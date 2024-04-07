package me.danlowe.pregnancytracker.ui.screen.currentweek

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import me.danlowe.database.DbUtils
import me.danlowe.database.prefs.PrefKey
import me.danlowe.models.TrimesterProgress
import me.danlowe.pregnancytracker.DbPregnancy
import me.danlowe.pregnancytracker.PregnancyQueries
import me.danlowe.pregnancytracker.R
import me.danlowe.utils.coroutines.AppDispatchers
import me.danlowe.utils.date.PREGNANCY_LENGTH_DAYS
import me.danlowe.utils.date.from3339StringToInstant
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class CurrentWeekScreenModel(
  pregnancyQueries: PregnancyQueries,
  dispatchers: AppDispatchers,
  appPreferences: DataStore<Preferences>,
) : ScreenModel {

  private val currentPregnancyIdFlow = appPreferences.data.map { preferences ->
    preferences[PrefKey.currentPregnancy] ?: DbUtils.ID_NO_VALUE
  }

  private val currentPregnancy: Flow<DbPregnancy> = currentPregnancyIdFlow
    .takeWhile { it != DbUtils.ID_NO_VALUE }
    .flatMapLatest { currentPregnancyId ->
      pregnancyQueries
        .selectById(currentPregnancyId)
        .asFlow()
        .mapToOne(dispatchers.io)
    }

  val state = currentPregnancy.map { pregnancy ->
    val pregnancyTime = PregnancyTime(pregnancy.dueDate)

    CurrentWeekState.Loaded(
      currentWeek = pregnancyTime.currentWeek,
      trimesterProgress = pregnancyTime.trimesterProgress,
      daysLeft = pregnancyTime.daysLeft,
      currentDayOf = pregnancyTime.daysIn,
      currentWeekImage = R.drawable.ic_blueberry,
    )
  }.flowOn(dispatchers.io)
    .stateIn(
      scope = screenModelScope,
      started = SharingStarted.Eagerly,
      initialValue = CurrentWeekState.Loading,
    )
}

data class PregnancyTime(
  val dueDate: String,
) {
  private val dueDateInstant = dueDate.from3339StringToInstant().atZone(ZoneId.systemDefault())
  private val conceptionDateInstant = dueDateInstant.minusDays(PREGNANCY_LENGTH_DAYS.toLong())
  private val currentTime = Instant.now().atZone(ZoneId.systemDefault())

  val currentWeek = ChronoUnit.WEEKS.between(conceptionDateInstant, currentTime).toInt()
  val daysLeft = ChronoUnit.DAYS.between(currentTime, dueDateInstant).toInt()
  val daysIn = PREGNANCY_LENGTH_DAYS - daysLeft
  val trimesterProgress = TrimesterProgress(currentWeek)
}
