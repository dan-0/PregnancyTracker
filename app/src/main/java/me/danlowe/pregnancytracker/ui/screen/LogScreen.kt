package me.danlowe.pregnancytracker.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.danlowe.models.LogEntry

class LogScreen(private val currentPregnancyId: Long) : Screen {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<LogScreenModel>()

    val state by screenModel.state.collectAsState(LogState.Loading)

    when (state) {
      LogState.Loading -> {
      }

      is LogState.Loaded -> {

      }
    }
  }
}

class LogScreenModel : ScreenModel {
  val _state = MutableStateFlow<LogState>(LogState.Loading)
  val state: StateFlow<LogState> = _state
}

sealed class LogState {
  object Loading : LogState()
  data class Loaded(
    val currentWeek: Int,
    val recentEntries: ImmutableList<LogEntry>,
  ) : LogState()
}

