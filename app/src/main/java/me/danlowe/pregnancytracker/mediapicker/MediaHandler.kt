package me.danlowe.pregnancytracker.mediapicker

import android.net.Uri
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import me.danlowe.utils.coroutines.AppDispatchers

class MediaHandler(
  private val dispatchers: AppDispatchers,
) {

  private val _requests = MutableSharedFlow<MediaRequestType>(0, 1)
  val requests: SharedFlow<MediaRequestType> = _requests

  private val mediaPickerResults = MutableSharedFlow<List<Uri>>(0, 2)

  private val cameraResults = MutableSharedFlow<Boolean>(0, 1)

  suspend fun requestMedia(
    mediaRequestType: MediaRequestType
  ): List<Uri> = withContext(dispatchers.io) {
    _requests.tryEmit(mediaRequestType)
    when (mediaRequestType) {
      is MediaRequestType.Camera -> {
        val isSuccessful = cameraResults.first()
        if (isSuccessful) {
          listOf(mediaRequestType.uri)
        } else {
          emptyList()
        }
      }
      is MediaRequestType.MediaPicker -> mediaPickerResults.first()
    }
  }

  fun handleMedia(uris: List<Uri>) {
    mediaPickerResults.tryEmit(uris)
  }

  fun handleCamera(isSuccess: Boolean) {
    cameraResults.tryEmit(isSuccess)
  }
}

sealed class MediaRequestType {
  data class Camera(val uri: Uri) : MediaRequestType()
  data object MediaPicker : MediaRequestType()
}