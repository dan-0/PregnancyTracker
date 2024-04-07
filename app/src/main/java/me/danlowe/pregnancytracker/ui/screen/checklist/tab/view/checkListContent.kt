package me.danlowe.pregnancytracker.ui.screen.checklist.tab.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import me.danlowe.pregnancytracker.ui.screen.checklist.tab.data.CheckListItem
import me.danlowe.pregnancytracker.ui.screen.checklist.tab.data.ChecklistTabEvent

@Composable
fun CheckListContent(
  items: ImmutableList<CheckListItem>,
  modifier: Modifier = Modifier,
  dispatch: (ChecklistTabEvent) -> Unit,
) {
  LazyColumn(
    modifier = modifier,
  ) {
    items.forEach {
      when (it) {
        is CheckListItem.Header -> {
          item("Checklist${it.checkList.id}") {
            CheckListHeader(it.checkList)
          }
        }

        is CheckListItem.Item -> {
          item("CheckItem${it.checkItem.id}") {
            CheckItemRow(it.checkItem, Modifier.fillMaxWidth()) { id, isChecked ->
              dispatch(ChecklistTabEvent.CheckItemChecked(id, isChecked))
            }
          }
        }
      }
    }
  }
}
