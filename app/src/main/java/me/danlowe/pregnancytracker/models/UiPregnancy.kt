package me.danlowe.pregnancytracker.models

import me.danlowe.pregnancytracker.DbPregnancy
import me.danlowe.utils.date.AppDateFormatter

data class UiPregnancy(
  val id: Long,
  val dueDate: String,
  val motherName: String,
) {
  companion object {
    fun fromDbPregnancy(dbPregnancy: DbPregnancy, appDateFormatter: me.danlowe.utils.date.AppDateFormatter): UiPregnancy {
      return UiPregnancy(
        id = dbPregnancy.id,
        dueDate = appDateFormatter.toLocalizedShortDate(dbPregnancy.dueDate),
        motherName = dbPregnancy.motherName,
      )
    }
  }
}
