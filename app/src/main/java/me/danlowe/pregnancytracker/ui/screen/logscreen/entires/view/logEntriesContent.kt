package me.danlowe.pregnancytracker.ui.screen.logscreen.entires.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import me.danlowe.models.LogEntry
import me.danlowe.pregnancytracker.R

@Composable
fun LogEntriesContent(
  recentEntries: ImmutableList<LogEntry>,
  modifier: Modifier = Modifier,
  navigateAddLog: () -> Unit,
) {
  Scaffold(
    floatingActionButton = {
      FloatingActionButton(onClick = { navigateAddLog() }) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = stringResource(R.string.add_log),
          modifier = Modifier.testTag("addFab"),
        )
      }
    },
    modifier = modifier,
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier.padding(paddingValues),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      item("topdivider") {
        Spacer(modifier = Modifier.height(16.dp))
      }
      items(
        items = recentEntries,
        key = { "LOG_ITEM${it.id}" },
      ) { entry ->
        LogEntryItem(
          entry,
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        )
      }
      item("bottomdivider") {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
