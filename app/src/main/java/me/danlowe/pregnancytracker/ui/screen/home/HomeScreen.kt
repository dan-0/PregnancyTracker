package me.danlowe.pregnancytracker.ui.screen.home

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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.collections.immutable.persistentListOf
import me.danlowe.pregnancytracker.ui.screen.currentweek.CurrentWeekScreen
import me.danlowe.pregnancytracker.ui.screen.home.views.AddPregnancyDialog
import me.danlowe.pregnancytracker.ui.screen.home.views.PregnancyItem
import me.danlowe.pregnancytracker.ui.screen.home.views.RemovePregnancyDialog

class HomeScreen : Screen {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<HomeScreenModel>()

    var showAddDialog by rememberSaveable {
      mutableStateOf(false)
    }
    var pregnancyForRemoval by rememberSaveable {
      mutableStateOf<Long?>(null)
    }
    val showRemovePregnancyDialog by rememberSaveable(pregnancyForRemoval) {
      mutableStateOf(pregnancyForRemoval != null)
    }

    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
      floatingActionButton = {
        FloatingActionButton(onClick = { showAddDialog = !showAddDialog }) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            modifier = Modifier.testTag("homeFab"),
          )
        }
      },
    ) { paddingValues ->
      if (showAddDialog) {
        AddPregnancyDialog(
          closeDialog = { showAddDialog = false },
          addNewPregnancy = { motherName, date ->
            screenModel.addPregnancy(motherName, date)
          },
        )
      }
      if (showRemovePregnancyDialog) {
        RemovePregnancyDialog(
          removePregnancy = {
            screenModel.deletePregnancy(pregnancyForRemoval!!)
          },
          onDismiss = {
            pregnancyForRemoval = null
          },
        )
      }
      val pregnancies = screenModel.items.collectAsState(persistentListOf())
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues),
      ) {
        items(pregnancies.value, key = { "pregnancies${it.id}" }) { pregnancy ->
          PregnancyItem(
            pregnancy = pregnancy,
            removeClicked = { pregnancyForRemoval = pregnancy.id },
            clicked = { navigator.push(CurrentWeekScreen(pregnancy.id)) },
          )
        }
      }
    }
  }
}
