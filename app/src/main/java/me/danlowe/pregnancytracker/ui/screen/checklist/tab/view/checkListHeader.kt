package me.danlowe.pregnancytracker.ui.screen.checklist.tab.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.danlowe.pregnancytracker.ui.screen.checklist.tab.data.CheckList

@Composable
fun CheckListHeader(
  checklist: CheckList,
  modifier: Modifier = Modifier,
) {
  Text(
    text = checklist.name,
    modifier = modifier,
  )
}
