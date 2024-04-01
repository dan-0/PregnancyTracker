package me.danlowe.pregnancytracker.ui.screen.logscreen.edit.data

import me.danlowe.pregnancytracker.ui.screen.logscreen.add.data.AttachmentType

sealed class EditLogEvent {
  data class UpdateLogEntry(val entry: String) : EditLogEvent()
  data class RequestAddAttachment(val type: AttachmentType) : EditLogEvent()
  data class DeleteAttachment(val uri: String) : EditLogEvent()
  data object SaveEdit : EditLogEvent()
  data object Back : EditLogEvent()
}