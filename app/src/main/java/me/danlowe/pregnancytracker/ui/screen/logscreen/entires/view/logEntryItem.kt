package me.danlowe.pregnancytracker.ui.screen.logscreen.entires.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.danlowe.models.LogEntry
import me.danlowe.pregnancytracker.ui.views.AttachmentImagePager
import me.danlowe.utils.date.toLocalizedShortDateTime

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
        .wrapContentHeight(),
    ) {
      if (entry.attachmentUris.isNotEmpty()) {
        AttachmentImagePager(
          imageUris = entry.attachmentUris,
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp),
        )
      }
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
            style = MaterialTheme.typography.labelSmall,
          )
        }
      }
    }
  }
}
