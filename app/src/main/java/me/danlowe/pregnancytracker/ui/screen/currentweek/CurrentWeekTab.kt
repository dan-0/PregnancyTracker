package me.danlowe.pregnancytracker.ui.screen.currentweek

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import me.danlowe.models.TrimesterProgress
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.screen.currentweek.views.TrimesterProgressView
import me.danlowe.pregnancytracker.ui.views.FullScreenLoading
import me.danlowe.utils.date.toLocalizedShortDate
import java.time.Instant

object CurrentWeekTab : Tab {
  private fun readResolve(): Any = CurrentWeekTab
  override val options: TabOptions
    @Composable
    get() {
      val icon = rememberVectorPainter(Icons.TwoTone.Home)
      val title = stringResource(R.string.home)
      return remember {
        TabOptions(
          index = 0u,
          title = title,
          icon = icon,
        )
      }
    }

  @Composable
  override fun Content() {
    val screenModel = LocalNavigator.currentOrThrow
      .getNavigatorScreenModel<CurrentWeekScreenModel>()

    val state by screenModel.state.collectAsState(CurrentWeekState.Loading)

    when (state) {
      CurrentWeekState.Loading -> {
        FullScreenLoading()
      }

      is CurrentWeekState.Loaded -> {
        val loadedState = state as CurrentWeekState.Loaded
        CurrentWeekSummary(
          currentWeek = loadedState.currentWeek,
          daysLeft = loadedState.daysLeft,
          currentWeekImage = loadedState.currentWeekImage,
          trimesterProgress = loadedState.trimesterProgress,
          daysIn = loadedState.currentDayOf,
          whatToExpect = loadedState.whatToExpectStringRes,
          modifier = Modifier.padding(16.dp),
        )
      }
    }
  }
}

@Composable
fun CurrentWeekSummary(
  currentWeek: Int,
  daysLeft: Int,
  daysIn: Int,
  trimesterProgress: TrimesterProgress,
  @DrawableRes currentWeekImage: Int,
  @StringRes whatToExpect: Int,
  modifier: Modifier = Modifier,
  currentDate: Instant = Instant.now(),
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Text(
      stringResource(
        R.string.current_week_date_days_left,
        currentDate.toLocalizedShortDate(),
        daysIn,
        daysLeft,
      ),
    )

    SummaryTopStatusRow(currentWeek, trimesterProgress, currentWeekImage)

    WhatToExpect(currentWeek, whatToExpect, modifier = Modifier.padding())
  }
}

@Composable
private fun SummaryTopStatusRow(
  currentWeek: Int,
  trimesterProgress: TrimesterProgress,
  currentWeekImage: Int,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    CurrentWeekSummaryItem(
      modifier = Modifier
        .height(200.dp)
        .weight(1f),
    ) {
      WeekTrimesterProgress(currentWeek, trimesterProgress)
    }
    CurrentWeekSummaryItem(
      modifier = Modifier
        .height(200.dp)
        .weight(1f),
    ) {
      CurrentSize(currentWeekImage)
    }
  }
}

@Composable
fun CurrentWeekSummaryItem(
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(CurrentWeekScreenDimens.basePadding),
    modifier = modifier
      .background(MaterialTheme.colorScheme.background, CurrentWeekScreenDimens.itemShape)
      .border(1.dp, MaterialTheme.colorScheme.outline, CurrentWeekScreenDimens.itemShape)
      .padding(CurrentWeekScreenDimens.basePadding),
  ) {
    content()
  }
}

@Composable
fun ColumnScope.WeekTrimesterProgress(
  currentWeek: Int,
  trimesterProgress: TrimesterProgress,
  modifier: Modifier = Modifier,
) {
  Text(stringResource(R.string.current_week, currentWeek))
  Image(
    modifier = modifier.size(100.dp),
    painter = painterResource(id = R.drawable.ic_baby_in_hands),
    contentDescription = null,
  )
  TrimesterProgressView(trimesterProgress)
}

@Composable
fun TrimesterProgressBar(currentProgress: Float, modifier: Modifier = Modifier) {
  LinearProgressIndicator(
    modifier = modifier,
    progress = { currentProgress },
  )
}

@Composable
fun ColumnScope.CurrentSize(
  @DrawableRes image: Int,
  modifier: Modifier = Modifier,
) {
  Text(stringResource(R.string.baby_size))
  Box(
    modifier = modifier.weight(1f),
    contentAlignment = Alignment.Center,
  ) {
    Image(
      modifier = Modifier.size(100.dp),
      painter = painterResource(id = image),
      contentDescription = null,
    )
  }
}

@Composable
fun WhatToExpect(
  currentWeek: Int,
  @StringRes whatToExpect: Int,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .border(1.dp, MaterialTheme.colorScheme.outline, CurrentWeekScreenDimens.itemShape)
      .padding(CurrentWeekScreenDimens.basePadding),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Text(
      text = stringResource(R.string.what_to_expect_week, currentWeek),
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = stringResource(id = whatToExpect),
    )
  }
}

object CurrentWeekScreenDimens {
  val basePadding = 8.dp
  val itemShape = RoundedCornerShape(8.dp)
}
