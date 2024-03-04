package me.danlowe.pregnancytracker.util.date

interface AppDateFormatter {
  fun toLocalizedShortDate(date: String): String

  /**
   * Date time picker returns a time in UTC, but we want to save it as the user's local date
   */
  fun utcLongDateToAdjustedLocalDate(date: Long): String
}
