package me.danlowe.pregnancytracker.ui.screen.checklist.tab.data

sealed class ChecklistTabEvent {
  data class CheckItemChecked(val itemId: Long, val checked: Boolean) : ChecklistTabEvent()
}
