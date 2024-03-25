package me.danlowe.pregnancytracker.ui.screen.logscreen.entires

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.danlowe.pregnancytracker.ui.screen.logscreen.LogModel
import me.danlowe.pregnancytracker.ui.screen.logscreen.LogState
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.AddLogScreen
import me.danlowe.pregnancytracker.ui.screen.logscreen.entires.view.LogEntriesContent
import me.danlowe.pregnancytracker.ui.views.FullScreenLoading

class LogEntriesScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow

    val screenModel = navigator.getNavigatorScreenModel<LogModel>()

    val state by screenModel.state.collectAsState(LogState.Loading)

    when (val currentState = state) {
      LogState.Loading -> {
        FullScreenLoading()
      }

      is LogState.Loaded -> {
        LogEntriesContent(currentState.recentEntries) {
          navigator.push(AddLogScreen(currentState.currentPregnancyId))
        }
      }
    }
  }
}
