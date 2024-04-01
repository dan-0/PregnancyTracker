package me.danlowe.pregnancytracker.ui.screen.logscreen.add

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.view.AddLogContent

class AddLogScreen(private val pregnancyId: Long) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow

    val addLogModel = getScreenModel<AddLogModel>()

    val attachments = addLogModel.attachments.collectAsState()

    val errors = addLogModel.attachmentError.collectAsState(null)

    val context = LocalContext.current
    LaunchedEffect(key1 = errors.value ?: "empty") {
      if (errors.value != null) {
        Toast.makeText(
          context,
          context.getString(R.string.error_accessing_camera),
          Toast.LENGTH_LONG,
        ).show()
      }
    }

    AddLogContent(
      attachments = attachments.value,
      onCancel = { navigator.pop() },
      addAttachments = addLogModel::requestAttachments,
      deleteAttachment = addLogModel::deleteAttachment,
      goBack = { navigator.pop() },
    ) { entry ->
      addLogModel.addLogEntry(pregnancyId, entry)
      navigator.pop()
    }
  }
}
