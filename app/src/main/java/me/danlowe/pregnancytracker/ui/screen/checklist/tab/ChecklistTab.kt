package me.danlowe.pregnancytracker.ui.screen.checklist.tab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Checklist
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.collections.immutable.persistentListOf
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.screen.checklist.tab.data.CheckListState
import me.danlowe.pregnancytracker.ui.screen.checklist.tab.data.ChecklistTabEvent
import me.danlowe.pregnancytracker.ui.screen.checklist.tab.view.CheckListContent

object ChecklistTab : Tab {
  private fun readResolve(): Any = ChecklistTab

  override val options: TabOptions
    @Composable
    get() {
      val icon = rememberVectorPainter(image = Icons.TwoTone.Checklist)
      return TabOptions(
        index = 1u,
        title = stringResource(R.string.checklists),
        icon = icon,
      )
    }

  @Composable
  override fun Content() {
    val model: ChecklistModel = LocalNavigator.currentOrThrow
      .getNavigatorScreenModel()

    val state = model.state.collectAsState(CheckListState(persistentListOf()))

    CheckListContent(state.value.items, Modifier.fillMaxSize()) {
      when (it) {
        is ChecklistTabEvent.CheckItemChecked -> {
          model.setChecked(it.itemId, it.checked)
        }
      }
    }
  }
}
