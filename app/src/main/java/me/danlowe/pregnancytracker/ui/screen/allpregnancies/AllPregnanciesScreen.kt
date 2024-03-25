package me.danlowe.pregnancytracker.ui.screen.allpregnancies

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.danlowe.pregnancytracker.models.UiPregnancy
import me.danlowe.pregnancytracker.ui.screen.allpregnancies.views.AddPregnancyDialog
import me.danlowe.pregnancytracker.ui.screen.allpregnancies.views.PregnancyItem
import me.danlowe.pregnancytracker.ui.screen.allpregnancies.views.RemovePregnancyDialog

class AllPregnanciesScreen : Screen {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<AllPregnanciesScreenModel>()

    AllPregnanciesContent(screenModel)
  }
}

@Composable
fun AllPregnanciesContent(
  screenModel: AllPregnanciesScreenModel,
  modifier: Modifier = Modifier,
) {
  var showAddDialog by rememberSaveable {
    mutableStateOf(false)
  }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(onClick = { showAddDialog = !showAddDialog }) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add",
          modifier = Modifier.testTag("addFab"),
        )
      }
    },
    modifier = modifier,
  ) { paddingValues ->

    val pregnancies = screenModel.items.collectAsState(persistentListOf())
    AllPregnanciesView(
      modifier = Modifier.fillMaxSize().padding(paddingValues),
      pregnancies = pregnancies.value,
      showAddDialog = showAddDialog,
      deletePregnancy = { screenModel.deletePregnancy(it) },
      setSelection = { screenModel.setActivePregnancy(it) },
      addPregnancy = { motherName, date -> screenModel.addPregnancy(motherName, date) },
      closeAddDialog = { showAddDialog = false },
    )
  }
}

@Composable
fun AllPregnanciesView(
  pregnancies: ImmutableList<UiPregnancy>,
  showAddDialog: Boolean,
  deletePregnancy: (Long) -> Unit,
  setSelection: (Long) -> Unit,
  addPregnancy: (String, Long) -> Unit,
  modifier: Modifier = Modifier,
  closeAddDialog: () -> Unit,
) {
  var pregnancyForRemoval by rememberSaveable {
    mutableStateOf<Long?>(null)
  }
  val showRemovePregnancyDialog by rememberSaveable(pregnancyForRemoval) {
    mutableStateOf(pregnancyForRemoval != null)
  }

  if (showAddDialog) {
    AddPregnancyDialog(
      closeDialog = closeAddDialog,
      addNewPregnancy = addPregnancy,
    )
  }
  if (showRemovePregnancyDialog) {
    RemovePregnancyDialog(
      removePregnancy = {
        deletePregnancy(pregnancyForRemoval!!)
      },
      onDismiss = {
        pregnancyForRemoval = null
      },
    )
  }
  LazyColumn(
    modifier = modifier,
  ) {
    items(pregnancies, key = { "pregnancies${it.id}" }) { pregnancy ->
      PregnancyItem(
        pregnancy = pregnancy,
        removeClicked = { pregnancyForRemoval = it },
        clicked = setSelection,
      )
    }
  }
}
