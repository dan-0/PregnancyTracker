package me.danlowe.pregnancytracker.ui.screen.logscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import me.danlowe.models.LogEntry
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.views.FullScreenLoading
import me.danlowe.utils.date.toLocalizedShortDateTime

object LogTab : Tab {
  private fun readResolve(): Any = LogTab
  override val options: TabOptions
    @Composable
    get() {
      val icon = rememberVectorPainter(Icons.Default.Edit)
      return TabOptions(
        index = 1u,
        title = stringResource(R.string.log),
        icon = icon,
      )
    }

  @Composable
  override fun Content() {
    val screenModel = getScreenModel<LogModel>()

    val state by screenModel.state.collectAsState(LogState.Loading)

    when (val currentState = state) {
      LogState.Loading -> {
        FullScreenLoading()
      }

      is LogState.Loaded -> {
        var showAddLogDialog by rememberSaveable {
          mutableStateOf(false)
        }
        Scaffold(
          floatingActionButton = {
            FloatingActionButton(onClick = { showAddLogDialog = true }) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_log),
                modifier = Modifier.testTag("addFab"),
              )
            }
          },
        ) { paddingValues ->

          if (showAddLogDialog) {
            Dialog(onDismissRequest = { showAddLogDialog = false }) {
              AddLogDialogContent(
                onCancel = { showAddLogDialog = false },
                submit = { entry ->
                  screenModel.handleEvent(
                    LogEvent.AddLogEntry(
                      pregnancyId = currentState.currentPregnancyId,
                      entry = entry,
                      attachmentUris = emptyList(),
                    ),
                  )
                  showAddLogDialog = false
                },
              )
            }
          }

          LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
          ) {
            item("topdivider") {
              Spacer(modifier = Modifier.height(16.dp))
            }
            items(
              items = currentState.recentEntries,
              key = { "LOG_ITEM${it.id}" },
            ) { entry ->
              LogEntryItem(entry)
            }
            item("bottomdivider") {
              Spacer(modifier = Modifier.height(16.dp))
            }
          }
        }
      }
    }
  }
}

@Composable
fun LogEntryItem(
  entry: LogEntry,
) {
  Card(
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .fillMaxWidth(),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Text(text = entry.entry)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
      ) {
        Text(
          text = entry.date.toLocalizedShortDateTime(),
          style = MaterialTheme.typography.labelSmall
        )
      }
    }
  }
}

@Composable
fun AddLogDialogContent(
  onCancel: () -> Unit,
  submit: (String) -> Unit,
) {
  var currentEntry by rememberSaveable(stateSaver = TextFieldValue.Saver) {
    mutableStateOf(TextFieldValue())
  }

  val focusRequester = remember { FocusRequester() }

    Column(
    modifier = Modifier
      .padding(16.dp)
      .fillMaxWidth()
      .focusRequester(focusRequester)
      .verticalScroll(rememberScrollState())
      .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Text(
      text = stringResource(R.string.add_log_entry),
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(horizontal = 16.dp),
    )

    OutlinedTextField(
      value = currentEntry,
      onValueChange = {
        currentEntry = it
      },
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
      minLines = 5,
      maxLines = 10,
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences,
        autoCorrect = true,
      ),
    )
    Row(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Button(onClick = onCancel) {
        Text(stringResource(id = R.string.cancel))
      }

      Button(
        onClick = {
          submit(currentEntry.text)
        },
      ) {
        Text(stringResource(R.string.submit))
      }
    }
  }
  LaunchedEffect("onEntryOnly") {
    focusRequester.requestFocus()
  }
}