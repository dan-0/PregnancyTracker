package me.danlowe.pregnancytracker.ui.screen.logscreen.edit

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.screen.logscreen.edit.data.EditLogEffect
import me.danlowe.pregnancytracker.ui.screen.logscreen.edit.data.EditLogEvent
import me.danlowe.pregnancytracker.ui.screen.logscreen.edit.view.EditLogContent
import org.koin.core.parameter.parametersOf

class EditLogScreen(
  private val logId: Long,
) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val model = getScreenModel<EditLogScreenModel> {
      parametersOf(logId)
    }

    val state by model.state.collectAsState()

    val effect by model.effects.collectAsState(initial = EditLogEffect.None)

    val context = LocalContext.current
    LaunchedEffect(key1 = effect) {
      when (effect) {
        is EditLogEffect.CameraAttachmentError -> {
          Toast.makeText(
            context,
            context.getString(R.string.error_accessing_camera),
            Toast.LENGTH_LONG,
          ).show()
        }
        EditLogEffect.None -> {
          /* ignore */
        }
      }
    }

    EditLogContent(state) { event ->
      when (event) {
        EditLogEvent.Back -> {
          navigator.pop()
        }
        is EditLogEvent.DeleteAttachment -> {
          model.deleteAttachment(event.uri)
        }
        is EditLogEvent.RequestAddAttachment -> {
          model.requestAttachments(event.type)
        }
        EditLogEvent.SaveEdit -> {
          model.saveEdit()
        }
        is EditLogEvent.UpdateLogEntry -> {
          model.updateLogEntry(event.entry)
        }
      }
    }
  }
}


