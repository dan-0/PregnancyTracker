package me.danlowe.pregnancytracker.ui.screen.logscreen.edit.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import me.danlowe.pregnancytracker.R

@Composable
fun EditLogButtonRow(
  canSave: Boolean,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
  save: () -> Unit,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.Bottom,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Button(onClick = onCancel) {
      Text(stringResource(id = R.string.cancel))
    }

    Button(
      onClick = save,
      enabled = canSave,
    ) {
      Text(stringResource(R.string.update))
    }
  }
}