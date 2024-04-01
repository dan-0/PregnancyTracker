package me.danlowe.pregnancytracker.ui.screen.logscreen.edit.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.data.AttachmentType
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.view.AddLogAttachments

@Composable
fun EditLogLoadedContent(
  entry: String,
  attachments: ImmutableList<String>,
  canSave: Boolean,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
  updateLogEntry: (String) -> Unit,
  deleteAttachment: (String) -> Unit,
  requestAddAttachment: (AttachmentType) -> Unit,
  save: () -> Unit,
) {
  var currentEntryText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
    mutableStateOf(TextFieldValue(entry))
  }

  LaunchedEffect(key1 = currentEntryText) {
    delay(200)
    updateLogEntry(currentEntryText.text)
  }

  Column(
    modifier = modifier
        .verticalScroll(rememberScrollState())
        .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
        .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {

    AddLogAttachments(
      attachments = attachments,
      modifier = Modifier.fillMaxWidth(),
      addAttachments = requestAddAttachment,
      deleteAttachment = deleteAttachment,
    )

    OutlinedTextField(
      value = currentEntryText,
      onValueChange = {
        currentEntryText = it
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

    EditLogButtonRow(
      canSave = canSave,
      onCancel = onBack,
      save = save,
      modifier = Modifier.fillMaxWidth()
    )
  }
}