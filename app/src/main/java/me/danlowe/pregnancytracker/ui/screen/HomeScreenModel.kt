package me.danlowe.pregnancytracker.ui.screen

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.danlowe.pregnancytracker.PregnancyQueries
import me.danlowe.pregnancytracker.coroutines.AppDispatchers
import me.danlowe.pregnancytracker.models.UiPregnancy
import me.danlowe.pregnancytracker.util.date.AppDateFormatter

class HomeScreenModel(
  private val pregnancyQueries: PregnancyQueries,
  private val dispatchers: AppDispatchers,
  private val appDateFormatter: AppDateFormatter,
) : ScreenModel {
  val items = pregnancyQueries.selectAll().asFlow().mapToList(dispatchers.io).map { dbPregnancies ->
    dbPregnancies.map { pregnancy ->
      UiPregnancy.fromDbPregnancy(pregnancy, appDateFormatter)
    }.toImmutableList()
  }

  fun addPregnancy(
    motherName: String,
    date: Long,
  ) {
    screenModelScope.launch(dispatchers.io) {
      val timeString = appDateFormatter.utcLongDateToAdjustedLocalDate(date)
      pregnancyQueries.insert(null, motherName.trim(), timeString)
    }
  }

  fun deletePregnancy(id: Long) {
    screenModelScope.launch(dispatchers.io) {
      pregnancyQueries.delete(id)
    }
  }
}
