package me.danlowe.pregnancytracker.util.date

import android.content.Context

class RealAppDateFormatter(private val context: Context) : AppDateFormatter {
  override fun toLocalizedShortDate(date: String): String {
    return date.toLocalizedShortDate(context)
  }

  override fun utcLongDateToAdjustedLocalDate(date: Long): String {
    return date.fromUtcLongToLocalDateInstant().to3339String()
  }
}
