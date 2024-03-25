package me.danlowe.pregnancytracker.ui.screen.logscreen.tab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.screen.logscreen.entires.LogEntriesScreen

object LogTab : Tab {
  private fun readResolve(): Any = LogTab
  override val options: TabOptions
    @Composable
    get() {
      val icon = rememberVectorPainter(Icons.TwoTone.Edit)
      return TabOptions(
        index = 1u,
        title = stringResource(R.string.log),
        icon = icon,
      )
    }

  @Composable
  override fun Content() {
    Navigator(
      screen = LogEntriesScreen(),
    ) {
      SlideTransition(navigator = it)
    }
  }
}
