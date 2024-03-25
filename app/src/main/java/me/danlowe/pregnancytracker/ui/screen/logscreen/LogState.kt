package me.danlowe.pregnancytracker.ui.screen.logscreen

import kotlinx.collections.immutable.ImmutableList
import me.danlowe.models.LogEntry

sealed class LogState {
  data object Loading : LogState()
  data class Loaded(
    val currentPregnancyId: Long,
    val recentEntries: ImmutableList<LogEntry>,
  ) : LogState()
}
