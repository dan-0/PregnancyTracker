package me.danlowe.pregnancytracker.ui.screen.logscreen.add

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.danlowe.pregnancytracker.mediapicker.MediaHandler
import me.danlowe.pregnancytracker.mediapicker.MediaRequestType
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.data.AttachmentStatus
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.data.AttachmentType
import me.danlowe.utils.coroutines.AppDispatchers

class AddLogModel(
  private val dispatchers: AppDispatchers,
  private val mediaHandler: MediaHandler,
  private val context: Context,
) : ScreenModel {

  private val _attachments = MutableStateFlow<ImmutableList<String>>(persistentListOf())
  val attachments: StateFlow<ImmutableList<String>> = _attachments

  fun requestAttachments(type: AttachmentType): AttachmentStatus {
    val request = when (type) {
      AttachmentType.Camera -> {
        val uri = createImageUri() ?: return AttachmentStatus.CameraImageError
        MediaRequestType.Camera(uri)
      }
      AttachmentType.MediaPicker -> MediaRequestType.MediaPicker
    }
    screenModelScope.launch(dispatchers.io) {
      val uris = mediaHandler.requestMedia(request)
      val current = _attachments.value
      val newUris = uris.map { it.toString() }
      _attachments.value = (current + newUris).distinct().toImmutableList()
    }

    return AttachmentStatus.None
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
