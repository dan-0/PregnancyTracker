package me.danlowe.pregnancytracker.ui.main

sealed class MainState {
  data object Loading : MainState()
  data class HasExistingPregnancy(val pregnancyId: Long) : MainState()
  data object AllPregnancies : MainState()
}
