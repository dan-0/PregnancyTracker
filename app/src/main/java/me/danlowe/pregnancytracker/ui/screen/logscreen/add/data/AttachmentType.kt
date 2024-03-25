package me.danlowe.pregnancytracker.ui.screen.logscreen.add.data

sealed class AttachmentType {
  data object Camera : AttachmentType()
  data object MediaPicker : AttachmentType()
}
