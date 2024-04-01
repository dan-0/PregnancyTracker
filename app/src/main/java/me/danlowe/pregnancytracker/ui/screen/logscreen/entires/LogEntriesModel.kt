package me.danlowe.pregnancytracker.ui.screen.logscreen.entires

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import me.danlowe.pregnancytracker.repo.log.LogRepo
import me.danlowe.utils.coroutines.AppDispatchers

class LogEntriesModel(
  dispatchers: AppDispatchers,
  logRepo: LogRepo,
) : ScreenModel {

  val state = logRepo.currentLogs
    .flowOn(dispatchers.io)
    .shareIn(
      scope = screenModelScope,
      started = SharingStarted.Eagerly,
      replay = 1,
    )
}
