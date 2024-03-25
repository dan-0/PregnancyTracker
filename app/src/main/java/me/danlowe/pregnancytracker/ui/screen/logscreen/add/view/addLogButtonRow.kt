package me.danlowe.pregnancytracker.ui.screen.logscreen.add.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import me.danlowe.pregnancytracker.R

@Composable
fun AddLogButtonRow(
  onCancel: () -> Unit,
  submit: (String) -> Unit,
  currentEntry: TextFieldValue,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
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
