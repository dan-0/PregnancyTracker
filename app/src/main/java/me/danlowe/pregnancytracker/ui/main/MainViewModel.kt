package me.danlowe.pregnancytracker.ui.main

import androidx.compose.ui.util.fastFold
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.danlowe.database.DbUtils
import me.danlowe.database.prefs.PrefKey
import me.danlowe.pregnancytracker.DbPregnancy
import me.danlowe.pregnancytracker.PregnancyQueries
import me.danlowe.utils.coroutines.AppDispatchers
import me.danlowe.utils.date.from3339StringToInstant

class MainViewModel(
  pregnancyQueries: PregnancyQueries,
  dispatchers: AppDispatchers,
  appPreferences: DataStore<Preferences>,
) : ViewModel() {

  val state: StateFlow<MainState> = pregnancyQueries.selectAll().asFlow()
    .mapToList(dispatchers.io).map { pregnancies ->
      findFirstActivePregnancy(pregnancies).let { firstActivePregnancy ->
        if (firstActivePregnancy == null) {
          MainState.AllPregnancies
        } else {
          appPreferences.edit { preferences ->
            preferences[PrefKey.currentPregnancy] = firstActivePregnancy.id
          }
          MainState.HasExistingPregnancy(firstActivePregnancy.id)
        }
      }
    }
    .flowOn(dispatchers.io)
    .stateIn(viewModelScope, SharingStarted.Eagerly, MainState.Loading)

  private fun findFirstActivePregnancy(pregnancies: List<DbPregnancy>): DbPregnancy? {
    val firstActivePregnancy: DbPregnancy? = pregnancies
      .fastFold<DbPregnancy, DbPregnancy?>(null) { acc, pregnancy ->
        when {
          acc == null -> pregnancy
          !isPregnancyActive(acc) -> pregnancy
          !isPregnancyActive(pregnancy) -> acc
          else -> lastAccessedPregnancy(acc, pregnancy)
        }
      }.let { pregnancy ->
        when {
          pregnancy != null && isPregnancyActive(pregnancy) -> pregnancy
          else -> null
        }
      }
    return firstActivePregnancy
  }

  private fun isPregnancyActive(pregnancy: DbPregnancy): Boolean {
    return DbUtils.longToBoolean(pregnancy.isActive)
  }

  private fun lastAccessedPregnancy(first: DbPregnancy, second: DbPregnancy): DbPregnancy {
    val firstAccessed = first.lastViewed.from3339StringToInstant()
    val secondAccessed = second.lastViewed.from3339StringToInstant()

    return if (firstAccessed.isAfter(secondAccessed)) {
      first
    } else {
      second
    }
  }
}
