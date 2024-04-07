package me.danlowe.pregnancytracker.ui.screen.checklist.tab

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.danlowe.database.DbUtils
import me.danlowe.pregnancytracker.CheckitemsQueries
import me.danlowe.pregnancytracker.ChecklistsQueries
import me.danlowe.pregnancytracker.ui.screen.checklist.tab.data.CheckItem
import me.danlowe.pregnancytracker.ui.screen.checklist.tab.data.CheckList
import me.danlowe.pregnancytracker.ui.screen.checklist.tab.data.CheckListState
import me.danlowe.utils.coroutines.AppDispatchers

class ChecklistModel(
  private val checkitemsQueries: CheckitemsQueries,
  private val checklistsQueries: ChecklistsQueries,
  private val dispatchers: AppDispatchers,
) : ScreenModel {
  val state: Flow<CheckListState> = checklistsQueries.selectAll()
    .asFlow()
    .mapToList(dispatchers.io)
    .combine(checkitemsQueries.selectAll().asFlow().mapToList(dispatchers.io)) { lists, items ->
      val t = lists.map { list ->
        CheckList(
          id = list.id,
          name = list.checklistName,
          items = items.filter { it.id.toInt() in list.checkItems }
            .map { item ->
              CheckItem(
                id = item.id,
                name = item.name,
                checked = DbUtils.longToBoolean(item.isChecked),
              )
            }.toImmutableList(),
        )
      }
      t.toImmutableList()
    }.map {
      CheckListState(it)
    }.flowOn(dispatchers.io)
    .stateIn(
      scope = screenModelScope,
      started = SharingStarted.Eagerly,
      initialValue = CheckListState(persistentListOf()),
    )

  fun setChecked(itemId: Long, checked: Boolean) {
    screenModelScope.launch(dispatchers.io) {
      checkitemsQueries.setCheck(DbUtils.booleanToLong(checked), itemId)
    }
  }

  fun updateItemPosition(itemId: Long, checklistId: Long, newPosition: Int) {
    screenModelScope.launch(dispatchers.io) {
      checklistsQueries.selectAll().asFlow().mapToList(dispatchers.io).collect {
        it.forEach { dbCheckList ->
          launch {
            if (dbCheckList.id == checklistId) {
              val newItems = dbCheckList.checkItems.toMutableList()
              newItems.remove(itemId.toInt())

              if (newPosition >= newItems.size) {
                newItems.add(itemId.toInt())
              } else {
                newItems.add(newPosition, itemId.toInt())
              }

              checklistsQueries.updateCheckItems(newItems, checklistId)
            } else {
              if (dbCheckList.checkItems.contains(itemId.toInt())) {
                val newItems = dbCheckList.checkItems.toMutableList()
                newItems.remove(itemId.toInt())
                checklistsQueries.updateCheckItems(newItems, dbCheckList.id)
              }
            }
          }
        }
      }
    }
  }
}
