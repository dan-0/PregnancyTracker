package me.danlowe.pregnancytracker.testutils

import me.danlowe.pregnancytracker.util.date.AppDateFormatter

class TestDateFormatter : AppDateFormatter {
  override fun toLocalizedShortDate(date: String): String {
    return date
  }

  override fun utcLongDateToAdjustedLocalDate(date: Long): String {
    return date.toString()
  }
}
