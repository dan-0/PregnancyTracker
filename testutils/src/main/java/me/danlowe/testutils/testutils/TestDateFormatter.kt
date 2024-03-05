package me.danlowe.testutils.testutils

import me.danlowe.utils.date.AppDateFormatter

class TestDateFormatter : AppDateFormatter {
  override fun toLocalizedShortDate(date: String): String {
    return date
  }

  override fun utcLongDateToAdjustedLocalDate(date: Long): String {
    return date.toString()
  }
}
