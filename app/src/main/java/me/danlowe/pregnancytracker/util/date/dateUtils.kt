package me.danlowe.pregnancytracker.util.date

import android.content.Context
import android.text.format.DateUtils
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

private val utcFormatter
  get() = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC))

fun String.from3339StringToInstant(): Instant {
  return utcFormatter.parse(this, Instant::from)
}

fun Instant.to3339String(): String {
  return utcFormatter.format(this)
}

fun String.from3339ToLocalizedTime(context: Context, flags: Int = DEFAULT_FLAGS): String {
  return from3339StringToInstant().toLocalizedTime(context, flags)
}

fun Instant.toLocalizedTime(context: Context, flags: Int = DEFAULT_FLAGS): String {
  return DateUtils.formatDateTime(context, Date.from(this).time, flags)
}

fun String.toLocalizedShortDate(context: Context): String {
  val instant = this.from3339StringToInstant()
  return DateUtils.formatDateTime(context, instant.toEpochMilli(), SHORT_DATE_FLAG)
}

@Composable
fun Instant.toLocalizedShortDate(): String {
  val context = LocalContext.current
  return DateUtils.formatDateTime(context, toEpochMilli(), SHORT_DATE_FLAG)
}

fun Long.fromUtcLongToLocalDateInstant(): Instant {
  return Instant.ofEpochMilli(this).let {
    val currentOffset = ZonedDateTime.now(ZoneId.systemDefault()).offset
    val utcOffset = ZoneOffset.UTC
    if (currentOffset.totalSeconds < utcOffset.totalSeconds) {
      it.plus(Duration.ofDays(1))
    } else {
      it
    }
  }
}

const val SHORT_DATE_FLAG = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_MONTH or
  DateUtils.FORMAT_SHOW_YEAR

const val DEFAULT_FLAGS = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_MONTH or
  DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
