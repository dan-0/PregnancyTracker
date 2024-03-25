package me.danlowe.pregnancytracker.ui.screen.logscreen.add.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.data.AttachmentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLogContent(
  attachments: ImmutableList<String>,
  onCancel: () -> Unit,
  addAttachments: (AttachmentType) -> Unit,
  modifier: Modifier = Modifier,
  submit: (entry: String) -> Unit,
) {
  var currentEntry by rememberSaveable(stateSaver = TextFieldValue.Saver) {
    mutableStateOf(TextFieldValue())
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.add_log_entry),
          )
        },
      )
    },
    modifier = modifier,
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
        .padding(paddingValues)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      AddLogAttachments(attachments, Modifier.fillMaxWidth(), addAttachments)

      OutlinedTextField(
        value = currentEntry,
        onValueChange = {
          currentEntry = it
        },
        modifier = Modifier
          .fillMaxWidth(),
        minLines = 5,
        maxLines = 10,
        label = { Text(stringResource(R.string.log_entry)) },
        keyboardOptions = KeyboardOptions(
          capitalization = KeyboardCapitalization.Sentences,
          autoCorrect = true,
        ),
      )

      AddLogButtonRow(onCancel, submit, currentEntry, Modifier.fillMaxWidth())
    }
  }
}
