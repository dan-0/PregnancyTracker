package me.danlowe.pregnancytracker.handlers.attachment

import me.danlowe.pregnancytracker.ui.screen.logscreen.add.data.AttachmentType

interface AttachmentHandler {
  suspend fun requestAttachments(
    type: AttachmentType,
  ): RequestResult

  sealed class RequestResult {
    data object CameraAttachmentError : RequestResult()
    data class Attachments(val uris: List<String>) : RequestResult()
  }
}
