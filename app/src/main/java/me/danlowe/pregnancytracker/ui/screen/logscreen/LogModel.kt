package me.danlowe.pregnancytracker.ui.screen.logscreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import me.danlowe.database.DbUtils
import me.danlowe.database.prefs.PrefKey
import me.danlowe.models.LogEntry
import me.danlowe.pregnancytracker.LogsQueries
import me.danlowe.utils.coroutines.AppDispatchers
import me.danlowe.utils.date.AppTime

class LogModel(
  private val logsQueries: LogsQueries,
  private val dispatchers: AppDispatchers,
  private val appTime: AppTime,
  appPreferences: DataStore<Preferences>,
) : ScreenModel {

  private val currentPregnancyIdFlow = appPreferences.data.map { preferences ->
    preferences[PrefKey.currentPregnancy] ?: DbUtils.ID_NO_VALUE
  }

  private val currentLogs = currentPregnancyIdFlow
    .takeWhile { it != DbUtils.ID_NO_VALUE }
    .flatMapLatest {
      logQueriesFlow(it)
    }.map { logs ->
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
        LogState.Loaded(
          recentEntries = entries,
        )
    }

  val state = currentLogs

  fun handleEvent(event: LogEvent) {
    screenModelScope.launch(dispatchers.io) {
      when (event) {
        is LogEvent.AddLogEntry -> addLogEntry(event)
        is LogEvent.UpdateLogEntry -> updateLogEntry(event)
        is LogEvent.DeleteLogEntry -> deleteLogEntry(event)
      }
    }
  }

  fun addLogEntry(addLogEntry: LogEvent.AddLogEntry) {
    screenModelScope.launch(dispatchers.io) {
      logsQueries.insert(
        id = null,
        pregnancyId = addLogEntry.pregnancyId,
        logDate = appTime.currentUtcTimeAsString(),
        logUpdatedDate = null,
        attachmentUris = addLogEntry.attachmentUris.joinToString(DbUtils.ATTACHMENT_SEPARATOR),
        entry = addLogEntry.entry
      )
    }
  }

  fun updateLogEntry(updateLogEntry: LogEvent.UpdateLogEntry) {
    screenModelScope.launch(dispatchers.io) {
      logsQueries.update(
        id = updateLogEntry.id,
        logUpdatedDate = appTime.currentUtcTimeAsString(),
        attachmentUris = updateLogEntry.attachmentUris.joinToString(DbUtils.ATTACHMENT_SEPARATOR),
        entry = updateLogEntry.entry
      )
    }
  }

  fun deleteLogEntry(deleteLogEntry: LogEvent.DeleteLogEntry) {
    screenModelScope.launch(dispatchers.io) {
      logsQueries.delete(deleteLogEntry.id)
    }
  }

  private fun logQueriesFlow(pregnancyID: Long) = logsQueries.selectByPregnancyId(pregnancyID)
    .asFlow()
    .mapToList(dispatchers.io)
}