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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import me.danlowe.models.TrimesterProgress
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.screen.currentweek.views.TrimesterProgressView
import me.danlowe.pregnancytracker.ui.views.FullScreenLoading
import me.danlowe.utils.date.toLocalizedShortDate
import org.koin.core.parameter.parametersOf
import java.time.Instant

class CurrentWeekScreen(private val currentPregnancyId: Long) : Screen {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<CurrentWeekScreenModel>(
      parameters = { parametersOf(currentPregnancyId) },
    )

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
          whatToExpect = loadedState.whatToExpectStringRes
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
  currentDate: Instant = Instant.now(),
  @DrawableRes currentWeekImage: Int,
  @StringRes whatToExpect: Int,
) {

  Column(
    modifier = Modifier.padding(16.dp),
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
      .padding(CurrentWeekScreenDimens.basePadding)
  ) {
    content()
  }
}

@Composable
fun ColumnScope.WeekTrimesterProgress(currentWeek: Int, trimesterProgress: TrimesterProgress) {
  Text("Week $currentWeek")
  Image(
    modifier = Modifier.size(100.dp),
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
fun ColumnScope.CurrentSize(@DrawableRes image: Int) {
  Text(stringResource(R.string.baby_size))
  Box(
    modifier = Modifier.weight(1f),
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
  modifier: Modifier = Modifier
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