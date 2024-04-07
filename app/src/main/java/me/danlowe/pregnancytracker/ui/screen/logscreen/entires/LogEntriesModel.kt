package me.danlowe.pregnancytracker.ui.screen.logscreen.entires

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import me.danlowe.pregnancytracker.repo.log.LogRepo
import me.danlowe.pregnancytracker.repo.log.LogState
import me.danlowe.utils.coroutines.AppDispatchers

class LogEntriesModel(
  dispatchers: AppDispatchers,
  logRepo: LogRepo,
) : ScreenModel {

  val state = logRepo.currentLogs
    .flowOn(dispatchers.io)
    .stateIn(
      scope = screenModelScope,
      started = SharingStarted.Eagerly,
      initialValue = LogState.Loading,
    )
}
