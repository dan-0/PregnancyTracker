package me.danlowe.pregnancytracker.ui.screen.logscreen.add

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.danlowe.pregnancytracker.handlers.attachment.AttachmentHandler
import me.danlowe.pregnancytracker.repo.log.LogRepo
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.data.AttachmentType
import me.danlowe.utils.coroutines.AppDispatchers

class AddLogModel(
  private val dispatchers: AppDispatchers,
  private val logRepo: LogRepo,
  private val attachmentHandler: AttachmentHandler,
) : ScreenModel {

  private val _attachments = MutableStateFlow<ImmutableList<String>>(persistentListOf())
  val attachments: StateFlow<ImmutableList<String>> = _attachments

  private val _attachmentError = MutableSharedFlow<Unit>()
  val attachmentError: SharedFlow<Unit?> = _attachmentError

  fun requestAttachments(type: AttachmentType) {
    screenModelScope.launch {
      when (val result = attachmentHandler.requestAttachments(type)) {
        is AttachmentHandler.RequestResult.Attachments -> {
          val current = _attachments.value
          _attachments.value = (current + result.uris).distinct().toImmutableList()
        }
        AttachmentHandler.RequestResult.CameraAttachmentError -> {
          _attachmentError.emit(Unit)
        }
      }
    }
  }

  fun deleteAttachment(uri: String) {
    screenModelScope.launch(dispatchers.io) {
      val current = _attachments.value.toMutableList()
      current.remove(uri)
      _attachments.value = current.toImmutableList()
    }
  }

  fun addLogEntry(
    pregnancyId: Long,
    entry: String,
  ) {
    screenModelScope.launch(dispatchers.io) {
      logRepo.addLogEntry(pregnancyId, entry, attachments.value)
    }
  }
}
