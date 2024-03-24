package me.danlowe.pregnancytracker.ui.screen.logscreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.views.FullScreenLoading

object LogTab : Tab {
  private fun readResolve(): Any = LogTab
  override val options: TabOptions
    @Composable
    get() {
      val icon = rememberVectorPainter(Icons.Default.Edit)
      return TabOptions(
        index = 1u,
        title = stringResource(R.string.log),
        icon = icon,
      )
    }

  @Composable
  override fun Content() {
    val screenModel = getScreenModel<LogModel>()

    val state by screenModel.state.collectAsState(LogState.Loading)

    when (state) {
      LogState.Loading -> {
        FullScreenLoading()
      }

      is LogState.Loaded -> {
        Text("do log stuff here")
      }
    }
  }
}
