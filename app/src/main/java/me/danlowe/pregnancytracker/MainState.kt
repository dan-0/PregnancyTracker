package me.danlowe.pregnancytracker

sealed class MainState {
  data object Loading : MainState()
  data class AddPregnancy(val pregnancyId: Long) : MainState()
  data object Home : MainState()
}