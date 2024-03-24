package me.danlowe.pregnancytracker.ui.screen.logscreen

sealed class LogEvent {
  data class AddLogEntry(
    val pregnancyId: Long,
    val entry: String,
    val attachmentUris: List<String>,
  ) : LogEvent()

  data class UpdateLogEntry(
    val id: Long,
    val pregnancyId: Long,
    val entry: String,
    val attachmentUris: List<String>,
  ) : LogEvent()
  data class DeleteLogEntry(
    val id: Long,
  ) : LogEvent()
}