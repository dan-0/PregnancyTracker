package me.danlowe.pregnancytracker.ui.screen.checklist.tab.data

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class CheckListState(
  val checkLists: ImmutableList<CheckList>,
) {
  val items = checkLists.flatMap { checkList ->
    listOf(CheckListItem.Header(checkList)) + checkList.items.map { item ->
      CheckListItem.Item(item, checkList.id)
    }
  }.toImmutableList()
}
