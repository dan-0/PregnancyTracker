package me.danlowe.pregnancytracker.ui.screen.logscreen.edit

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.danlowe.pregnancytracker.handlers.attachment.AttachmentHandler
import me.danlowe.pregnancytracker.repo.log.LogRepo
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.data.AttachmentType
import me.danlowe.pregnancytracker.ui.screen.logscreen.edit.data.EditLogEffect
import me.danlowe.pregnancytracker.ui.screen.logscreen.edit.data.EditLogState
import me.danlowe.utils.coroutines.AppDispatchers

class EditLogScreenModel(
  private val logRepo: LogRepo,
  private val dispatchers: AppDispatchers,
  private val attachmentHandler: AttachmentHandler,
  private val logId: Long,
) : ScreenModel {
  private val _state = MutableStateFlow<EditLogState>(EditLogState.Loading)
  val state: StateFlow<EditLogState> = _state

  private val _effects = MutableSharedFlow<EditLogEffect>(0, 1, BufferOverflow.DROP_OLDEST)
  val effects: SharedFlow<EditLogEffect> = _effects

  init {
    screenModelScope.launch {
      val entry = logRepo.getLogEntry(logId)

      _state.value = if (entry != null) {
        EditLogState.Loaded(entry, entry)
      } else {
        EditLogState.Error
      }
    }
  }

  fun requestAttachments(type: AttachmentType) {
    screenModelScope.launch {
      when (val result = attachmentHandler.requestAttachments(type)) {
        is AttachmentHandler.RequestResult.Attachments -> {
          val currentState = state.value as EditLogState.Loaded
          val newState = currentState.copy(
            workingEntry = currentState.workingEntry.copy(
              attachmentUris = (currentState.workingEntry.attachmentUris + result.uris)
                .distinct()
                .toImmutableList(),
            ),
          )
          _state.value = newState
        }

        AttachmentHandler.RequestResult.CameraAttachmentError -> {
          _effects.emit(EditLogEffect.CameraAttachmentError)
        }
      }
    }
  }

  fun deleteAttachment(uri: String) {
    screenModelScope.launch(dispatchers.io) {
      val currentState = state.value as EditLogState.Loaded
      val newState = currentState.copy(
        workingEntry = currentState.workingEntry.copy(
          attachmentUris = currentState.workingEntry.attachmentUris.filterNot {
            it == uri
          }.toImmutableList(),
        ),
      )
      _state.value = newState
    }
  }

  fun updateLogEntry(entry: String) {
    val currentState = state.value as EditLogState.Loaded
    val newState = currentState.copy(
      workingEntry = currentState.workingEntry.copy(entry = entry),
    )
    _state.value = newState
  }

  fun saveEdit() {
    screenModelScope.launch(dispatchers.io) {
      val currentState = state.value as EditLogState.Loaded

      if (currentState.workingEntry == currentState.goldEntry) return@launch

      logRepo.updateLogEntry(
        entryId = currentState.workingEntry.id,
        attachmentUris = currentState.workingEntry.attachmentUris,
        entryText = currentState.workingEntry.entry,
      )
    }
  }
}

