package me.danlowe.pregnancytracker.ui.screen.logscreen.edit.data

sealed class EditLogEffect {
  data object CameraAttachmentError : EditLogEffect()
  data object None : EditLogEffect()
}
