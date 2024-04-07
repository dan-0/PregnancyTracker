package me.danlowe.pregnancytracker.ui.screen.checklist.tab.data

sealed class CheckListItem {
  data class Header(val checkList: CheckList) : CheckListItem()
  data class Item(val checkItem: CheckItem, val checkListId: Long) : CheckListItem()
}
