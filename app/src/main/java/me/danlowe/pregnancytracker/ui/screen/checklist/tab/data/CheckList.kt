package me.danlowe.pregnancytracker.ui.screen.checklist.tab.data

import kotlinx.collections.immutable.ImmutableList

data class CheckList(
  val id: Long,
  val name: String,
  val items: ImmutableList<CheckItem>,
)
