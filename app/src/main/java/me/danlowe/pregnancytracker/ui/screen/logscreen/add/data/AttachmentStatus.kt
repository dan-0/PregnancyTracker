package me.danlowe.pregnancytracker.ui.screen.logscreen.add.data

sealed class AttachmentStatus {
  data object CameraImageError : AttachmentStatus()
  data object None : AttachmentStatus()
}
