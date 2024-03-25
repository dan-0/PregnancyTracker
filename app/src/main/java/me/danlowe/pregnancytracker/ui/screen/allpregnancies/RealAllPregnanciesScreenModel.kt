package me.danlowe.pregnancytracker.ui.screen.allpregnancies

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import me.danlowe.database.DbUtils
import me.danlowe.database.prefs.PrefKey
import me.danlowe.pregnancytracker.PregnancyQueries
import me.danlowe.pregnancytracker.models.UiPregnancy
import me.danlowe.utils.coroutines.AppDispatchers
import me.danlowe.utils.date.AppDateFormatter
import me.danlowe.utils.date.AppTime

class RealAllPregnanciesScreenModel(
  private val pregnancyQueries: PregnancyQueries,
  private val dispatchers: AppDispatchers,
  private val appDateFormatter: AppDateFormatter,
  private val appTime: AppTime,
  private val dataStore: DataStore<Preferences>,
) : AllPregnanciesScreenModel {
  override val items = pregnancyQueries
    .selectAll()
    .asFlow()
    .mapToList(dispatchers.io)
    .map { dbPregnancies ->
      dbPregnancies.map { pregnancy ->
        UiPregnancy.fromDbPregnancy(pregnancy, appDateFormatter)
      }.toImmutableList()
    }.flowOn(dispatchers.io)
    .shareIn(
      scope = screenModelScope,
      started = SharingStarted.Eagerly,
      replay = 1
    )

  override fun addPregnancy(
    motherName: String,
    date: Long,
  ) {
    screenModelScope.launch(dispatchers.io) {
      val timeString = appDateFormatter.utcLongDateToAdjustedLocalDate(date)
      pregnancyQueries.insert(
        null,
        motherName.trim(),
        timeString,
        DbUtils.booleanToLong(true),
        appTime.currentUtcTimeAsString(),
      )
    }
  }

  override fun deletePregnancy(id: Long) {
    screenModelScope.launch(dispatchers.io) {
      pregnancyQueries.delete(id)
    }
  }

  override fun setActivePregnancy(id: Long) {
    screenModelScope.launch(dispatchers.io) {
      dataStore.edit { preferences ->
        preferences[PrefKey.currentPregnancy] = id
      }
    }
  }
}

