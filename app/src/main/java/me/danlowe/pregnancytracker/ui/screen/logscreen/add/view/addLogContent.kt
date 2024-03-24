package me.danlowe.pregnancytracker.ui.screen.logscreen.add.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import me.danlowe.pregnancytracker.R

@Composable
fun AddLogContent(
  onCancel: () -> Unit,
  submit: (String) -> Unit,
) {
  var currentEntry by rememberSaveable(stateSaver = TextFieldValue.Saver) {
    mutableStateOf(TextFieldValue())
  }

  val focusRequester = remember { FocusRequester() }

  Column(
    modifier = Modifier
      .fillMaxSize()
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
        .weight(1f)
        .fillMaxWidth(),
      verticalAlignment = Alignment.Bottom,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Button(onClick = onCancel) {
        Text(stringResource(id = R.string.cancel))
      }

      Button(onClick = { submit(currentEntry.text) }) {
        Text(stringResource(R.string.submit))
      }
    }
  }
  LaunchedEffect("onEntryOnly") {
    focusRequester.requestFocus()
  }
}