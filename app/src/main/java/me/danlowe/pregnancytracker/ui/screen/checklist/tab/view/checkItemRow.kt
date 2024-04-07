package me.danlowe.pregnancytracker.ui.screen.checklist.tab.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.danlowe.pregnancytracker.ui.screen.checklist.tab.data.CheckItem

@Composable
fun CheckItemRow(
  checkItem: CheckItem,
  modifier: Modifier = Modifier,
  handleItemChecked: (Long, Boolean) -> Unit,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Checkbox(
      checked = checkItem.checked,
      onCheckedChange = {
        handleItemChecked(checkItem.id, it)
      },
    )
    Text(text = checkItem.name)
  }
}
