package me.danlowe.pregnancytracker.repo.log

import kotlinx.coroutines.flow.Flow
import me.danlowe.models.LogEntry

interface LogRepo {

  val currentLogs: Flow<LogState>

  suspend fun addLogEntry(
    pregnancyId: Long,
    entryText: String,
    attachmentUris: List<String>,
  )

  suspend fun updateLogEntry(
    entryId: Long,
    attachmentUris: List<String>,
    entryText: String,
  )

  suspend fun deleteLogEntry(id: Long)

  suspend fun getLogEntry(id: Long): LogEntry?
}
