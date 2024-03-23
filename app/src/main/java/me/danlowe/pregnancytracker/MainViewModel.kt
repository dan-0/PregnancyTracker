package me.danlowe.pregnancytracker

import androidx.compose.ui.util.fastFold
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
import me.danlowe.utils.coroutines.AppDispatchers
import me.danlowe.utils.date.from3339StringToInstant

class MainViewModel(
  pregnancyQueries: PregnancyQueries,
  dispatchers: AppDispatchers,
) : ViewModel() {

  val state: StateFlow<MainState> = pregnancyQueries.selectAll().asFlow()
    .mapToList(dispatchers.io).map { pregnancies ->
      findFirstActivePregnancy(pregnancies).let { firstActivePregnancy ->
        if (firstActivePregnancy == null) {
          MainState.AllPregnancies
        } else {
          MainState.AddPregnancy(firstActivePregnancy.id)
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