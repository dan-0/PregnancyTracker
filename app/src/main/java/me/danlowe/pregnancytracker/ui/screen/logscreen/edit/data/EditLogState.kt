package me.danlowe.pregnancytracker.ui.screen.logscreen.edit.data

import me.danlowe.models.LogEntry

sealed class EditLogState {
  data object Loading : EditLogState()
  data object Error : EditLogState()
  data class Loaded(val workingEntry: LogEntry, val goldEntry: LogEntry) : EditLogState() {
    val canSave = workingEntry != goldEntry
  }
}
