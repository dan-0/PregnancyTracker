package me.danlowe.pregnancytracker.repo.log

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.withContext
import me.danlowe.database.DbUtils
import me.danlowe.database.prefs.PrefKey
import me.danlowe.models.LogEntry
import me.danlowe.pregnancytracker.LogsQueries
import me.danlowe.utils.coroutines.AppDispatchers
import me.danlowe.utils.date.AppTime

class RealLogRepo(
  private val logsQueries: LogsQueries,
  private val dispatchers: AppDispatchers,
  private val appTime: AppTime,
  appPreferences: DataStore<Preferences>,
) : LogRepo {

  private val currentPregnancyIdFlow = appPreferences.data.map { preferences ->
    preferences[PrefKey.currentPregnancy] ?: DbUtils.ID_NO_VALUE
  }

  override val currentLogs: Flow<LogState>
    get() = currentPregnancyIdFlow
      .takeWhile { it != DbUtils.ID_NO_VALUE }
      .flatMapLatest {
        logQueriesFlow(it).map { logs ->
          logs to it
        }
      }.map { (logs, pregnancyId) ->
        val entries = logs.map {
          val attachmentUris = it.attachmentUris?.split(DbUtils.ATTACHMENT_SEPARATOR)
          LogEntry(
            id = it.id,
            pregnancyId = it.pregnancyId,
            attachmentUris = attachmentUris?.toImmutableList() ?: persistentListOf(),
            date = it.logDate,
            updatedDate = it.logUpdatedDate,
            entry = it.entry,
          )
        }.toImmutableList()

        // There seems to be a compiler bug where despite an explicit LogState type, the compiler
        // will only infer the type as LogState.Loaded unless we provide an alternate
        if (false) {
          LogState.Loading
        } else {
          LogState.Loaded(
            currentPregnancyId = pregnancyId,
            recentEntries = entries,
          )
        }
      }.onStart { emit(LogState.Loading) }

  override suspend fun addLogEntry(
    pregnancyId: Long,
    entryText: String,
    attachmentUris: List<String>,
  ) = withContext(dispatchers.io) {
    logsQueries.insert(
      id = null,
      pregnancyId = pregnancyId,
      logDate = appTime.currentUtcTimeAsString(),
      logUpdatedDate = null,
      attachmentUris = attachmentUris.let {
        if (it.isEmpty()) {
          null
        } else {
          it.joinToString(DbUtils.ATTACHMENT_SEPARATOR)
        }
      },
      entry = entryText,
    )
  }

  override suspend fun updateLogEntry(
    entryId: Long,
    attachmentUris: List<String>,
    entryText: String,
  ) = withContext(dispatchers.io) {
    val attachments = if (attachmentUris.filterNot { it.isBlank() }.isEmpty()) {
      null
    } else {
      attachmentUris.joinToString(DbUtils.ATTACHMENT_SEPARATOR)
    }
    logsQueries.update(
      id = entryId,
      logUpdatedDate = appTime.currentUtcTimeAsString(),
      attachmentUris = attachments,
      entry = entryText,
    )
  }

  override suspend fun deleteLogEntry(id: Long) = withContext(dispatchers.io) {
    logsQueries.delete(id)
  }

  override suspend fun getLogEntry(id: Long): LogEntry? = withContext(dispatchers.io) {
    val entry = logsQueries.selectByLogId(id).executeAsOneOrNull()
    entry ?: return@withContext null

    return@withContext LogEntry(
      id = entry.id,
      pregnancyId = entry.pregnancyId,
      attachmentUris = entry.attachmentUris
        ?.split(DbUtils.ATTACHMENT_SEPARATOR)
        ?.toImmutableList() ?: persistentListOf(),
      date = entry.logDate,
      updatedDate = entry.logUpdatedDate,
      entry = entry.entry,
    )
  }

  private fun logQueriesFlow(pregnancyID: Long) = logsQueries.selectByPregnancyId(pregnancyID)
    .asFlow()
    .mapToList(dispatchers.io)
}
