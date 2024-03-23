package me.danlowe.pregnancytracker.ui.screen.allpregnancies

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.Flow
import me.danlowe.pregnancytracker.models.UiPregnancy

interface AllPregnanciesScreenModel : ScreenModel {
  val items: Flow<List<UiPregnancy>>
  fun addPregnancy(motherName: String, date: Long)
  fun deletePregnancy(id: Long)
}