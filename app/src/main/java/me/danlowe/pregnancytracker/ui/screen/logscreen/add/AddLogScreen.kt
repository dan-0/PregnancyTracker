package me.danlowe.pregnancytracker.ui.screen.logscreen.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.danlowe.pregnancytracker.ui.screen.logscreen.LogEvent
import me.danlowe.pregnancytracker.ui.screen.logscreen.LogModel
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.view.AddLogContent

class AddLogScreen(private val pregnancyId: Long) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val logModel = navigator.getNavigatorScreenModel<LogModel>()

    val addLogModel = getScreenModel<AddLogModel>()

    val attachments = addLogModel.attachments.collectAsState()

    AddLogContent(
      attachments = attachments.value,
      onCancel = { navigator.pop() },
      addAttachments = addLogModel::requestAttachments,
    ) { entry ->
      logModel.handleEvent(LogEvent.AddLogEntry(pregnancyId, entry, attachments.value))
      navigator.pop()
    }
  }
}

