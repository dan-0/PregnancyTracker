package me.danlowe.pregnancytracker.ui.screen.currentweek

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.danlowe.models.TrimesterProgress
import me.danlowe.pregnancytracker.DbPregnancy
import me.danlowe.pregnancytracker.PregnancyQueries
import me.danlowe.pregnancytracker.R
import me.danlowe.utils.coroutines.AppDispatchers
import me.danlowe.utils.date.AppDateFormatter
import me.danlowe.utils.date.PREGNANCY_LENGTH_DAYS
import me.danlowe.utils.date.from3339StringToInstant
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class CurrentWeekScreenModel(
  private val pregnancyQueries: PregnancyQueries,
  private val dispatchers: AppDispatchers,
  private val appDateFormatter: AppDateFormatter,
  currentPregnancyId: Long,
) : ScreenModel {

  private val currentPregnancy: Flow<DbPregnancy> = pregnancyQueries
    .selectById(currentPregnancyId)
    .asFlow()
    .mapToOne(dispatchers.io)

  val state = currentPregnancy.map { pregnancy ->
    val dueDateInstant = pregnancy.dueDate.from3339StringToInstant().atZone(ZoneId.systemDefault())
    val conceptionDateInstant = dueDateInstant.minusDays(PREGNANCY_LENGTH_DAYS.toLong())
    val currentTime = Instant.now().atZone(ZoneId.systemDefault())

    val currentWeek = ChronoUnit.WEEKS.between(conceptionDateInstant, currentTime).toInt()
    val daysLeft = ChronoUnit.DAYS.between(currentTime, dueDateInstant).toInt()
    val daysIn = PREGNANCY_LENGTH_DAYS - daysLeft

    CurrentWeekState.Loaded(
      currentWeek = currentWeek,
      trimesterProgress = TrimesterProgress(currentWeek),
      daysLeft = daysLeft,
      currentDayOf = daysIn,
      currentWeekImage = R.drawable.ic_blueberry,
    )
  }.flowOn(dispatchers.io)
}