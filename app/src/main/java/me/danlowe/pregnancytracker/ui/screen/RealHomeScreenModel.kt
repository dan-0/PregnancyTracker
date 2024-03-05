package me.danlowe.pregnancytracker.ui.screen

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.danlowe.pregnancytracker.PregnancyQueries
import me.danlowe.pregnancytracker.models.UiPregnancy
import me.danlowe.utils.coroutines.AppDispatchers
import me.danlowe.utils.date.AppDateFormatter

class RealHomeScreenModel(
  private val pregnancyQueries: PregnancyQueries,
  private val dispatchers: AppDispatchers,
  private val appDateFormatter: AppDateFormatter,
) : HomeScreenModel {
  override val items = pregnancyQueries
    .selectAll()
    .asFlow()
    .mapToList(dispatchers.io)
    .map { dbPregnancies ->
      dbPregnancies.map { pregnancy ->
        UiPregnancy.fromDbPregnancy(pregnancy, appDateFormatter)
      }.toImmutableList()
    }

  override fun addPregnancy(
    motherName: String,
    date: Long,
  ) {
    screenModelScope.launch(dispatchers.io) {
      val timeString = appDateFormatter.utcLongDateToAdjustedLocalDate(date)
      pregnancyQueries.insert(null, motherName.trim(), timeString)
    }
  }

  override fun deletePregnancy(id: Long) {
    screenModelScope.launch(dispatchers.io) {
      pregnancyQueries.delete(id)
    }
  }
}

interface HomeScreenModel : ScreenModel {
  val items: Flow<List<UiPregnancy>>
  fun addPregnancy(motherName: String, date: Long)
  fun deletePregnancy(id: Long)
}
