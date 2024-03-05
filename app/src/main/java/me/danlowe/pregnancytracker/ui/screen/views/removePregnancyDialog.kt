package me.danlowe.pregnancytracker.ui.screen.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import me.danlowe.pregnancytracker.R

@Composable
fun RemovePregnancyDialog(
  removePregnancy: () -> Unit,
  onDismiss: () -> Unit,
) {
  Dialog(onDismissRequest = onDismiss) {
    RemovePregnancyDialogContent(
      onDismiss = onDismiss,
      removePregnancy = removePregnancy,
    )
  }
}

@Composable
fun RemovePregnancyDialogContent(
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  removePregnancy: () -> Unit,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
  ) {
    Text(
      text = stringResource(R.string.please_confirm),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.testTag("removePregnancyDialogTitle"),
    )
    Text(
      text = stringResource(R.string.confirm_delete_pregnancy),
      modifier = Modifier.testTag("removePregnancyDialogPrompt"),
    )
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
    ) {
      Button(
        onClick = {
          onDismiss()
        },
        modifier = Modifier.testTag("cancelButton"),
      ) {
        Text(
          text = stringResource(R.string.cancel),
        )
      }
      Button(
        onClick = {
          removePregnancy()
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.error,
          contentColor = MaterialTheme.colorScheme.onError,
        ),
        modifier = Modifier.testTag("deleteButton"),
      ) {
        Text(
          text = stringResource(R.string.confirm),
        )
      }
    }
  }
}
