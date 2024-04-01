package me.danlowe.pregnancytracker.handlers.attachment

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.withContext
import me.danlowe.pregnancytracker.handlers.mediapicker.MediaHandler
import me.danlowe.pregnancytracker.handlers.mediapicker.MediaRequestType
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.data.AttachmentType
import me.danlowe.utils.coroutines.AppDispatchers

class RealAttachmentHandler(
  private val context: Context,
  private val dispatchers: AppDispatchers,
  private val mediaHandler: MediaHandler,
) : AttachmentHandler {
  override suspend fun requestAttachments(
    type: AttachmentType,
  ): AttachmentHandler.RequestResult = withContext(dispatchers.io) {
    val request = when (type) {
      AttachmentType.Camera -> {
        val uri = createImageUri() ?: run {
          return@withContext AttachmentHandler.RequestResult.CameraAttachmentError
        }
        MediaRequestType.Camera(uri)
      }

      AttachmentType.MediaPicker -> MediaRequestType.MediaPicker
    }
    val uris = mediaHandler.requestMedia(request).map { it.toString() }
    return@withContext AttachmentHandler.RequestResult.Attachments(uris)
  }

  private fun createImageUri(): Uri? {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
      put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
      put(MediaStore.MediaColumns.DISPLAY_NAME, "image${System.currentTimeMillis()}.jpeg")
    }
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
  }
}
