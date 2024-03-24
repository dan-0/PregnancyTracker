package me.danlowe.pregnancytracker.ui.screen.logscreen.add

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.danlowe.pregnancytracker.ui.screen.logscreen.LogEvent
import me.danlowe.pregnancytracker.ui.screen.logscreen.LogModel
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.view.AddLogContent

class AddLogScreen(private val pregnancyId: Long, ) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val screenModel = navigator.getNavigatorScreenModel<LogModel>()

    AddLogContent(onCancel = { navigator.pop() }) {
      screenModel.handleEvent(LogEvent.AddLogEntry(pregnancyId, it, emptyList()))
      navigator.pop()
    }
  }
}