package me.danlowe.pregnancytracker.ui.screen.logscreen.edit.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.screen.logscreen.edit.data.EditLogEvent
import me.danlowe.pregnancytracker.ui.screen.logscreen.edit.data.EditLogState
import me.danlowe.pregnancytracker.ui.views.FullScreenLoading

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditLogContent(
  state: EditLogState,
  modifier: Modifier = Modifier,
  dispatch: (EditLogEvent) -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back),
            modifier = Modifier.clickable {
              dispatch(EditLogEvent.Back)
            },
          )
        },
        title = {
          Text(
            text = stringResource(R.string.edit_log_entry),
          )
        },
      )
    },
    modifier = modifier,
  ) { paddingValues ->
    when (val currentState = state) {
      is EditLogState.Loading -> {
        FullScreenLoading(modifier = Modifier.padding(paddingValues))
      }

      EditLogState.Error -> {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
          verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Text(text = stringResource(R.string.error_loading_log_entry))
          Button(onClick = { dispatch(EditLogEvent.Back) }) {
            Text(stringResource(id = R.string.back))
          }
        }
      }

      is EditLogState.Loaded -> {
        EditLogLoadedContent(
          modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
          entry = currentState.workingEntry.entry,
          attachments = currentState.workingEntry.attachmentUris,
          canSave = currentState.canSave,
          onBack = { dispatch(EditLogEvent.Back) },
          updateLogEntry = { dispatch(EditLogEvent.UpdateLogEntry(it)) },
          requestAddAttachment = { dispatch(EditLogEvent.RequestAddAttachment(it)) },
          deleteAttachment = { dispatch(EditLogEvent.DeleteAttachment(it)) },
        ) {
          dispatch(EditLogEvent.SaveEdit)
          dispatch(EditLogEvent.Back)
        }
      }
    }
  }
}
