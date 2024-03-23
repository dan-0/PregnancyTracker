package me.danlowe.pregnancytracker.ui.screen.currentweek

import androidx.annotation.DrawableRes
import me.danlowe.models.TrimesterProgress
import me.danlowe.pregnancytracker.R

sealed class CurrentWeekState {
  data object Loading : CurrentWeekState()
  data class Loaded(
    val currentWeek: Int,
    val trimesterProgress: TrimesterProgress,
    val daysLeft: Int,
    val currentDayOf: Int,
    @DrawableRes val currentWeekImage: Int,
  ) : CurrentWeekState()
}

val CurrentWeekState.Loaded.whatToExpectStringRes: Int
  get() {
    return when (currentWeek) {
      1 -> R.string.week_1_what_to_expect
      2 -> R.string.week_2_what_to_expect
      3 -> R.string.week_3_what_to_expect
      4 -> R.string.week_4_what_to_expect
      5 -> R.string.week_5_what_to_expect
      6 -> R.string.week_6_what_to_expect
      7 -> R.string.week_7_what_to_expect
      8 -> R.string.week_8_what_to_expect
      9 -> R.string.week_9_what_to_expect
      10 -> R.string.week_10_what_to_expect
      11 -> R.string.week_11_what_to_expect
      12 -> R.string.week_12_what_to_expect
      13 -> R.string.week_13_what_to_expect
      14 -> R.string.week_14_what_to_expect
      15 -> R.string.week_15_what_to_expect
      16 -> R.string.week_16_what_to_expect
      17 -> R.string.week_17_what_to_expect
      18 -> R.string.week_18_what_to_expect
      19 -> R.string.week_19_what_to_expect
      20 -> R.string.week_20_what_to_expect
      21 -> R.string.week_21_what_to_expect
      22 -> R.string.week_22_what_to_expect
      23 -> R.string.week_23_what_to_expect
      24 -> R.string.week_24_what_to_expect
      25 -> R.string.week_25_what_to_expect
      26 -> R.string.week_26_what_to_expect
      27 -> R.string.week_27_what_to_expect
      28 -> R.string.week_28_what_to_expect
      29 -> R.string.week_29_what_to_expect
      30 -> R.string.week_30_what_to_expect
      31 -> R.string.week_31_what_to_expect
      32 -> R.string.week_32_what_to_expect
      33 -> R.string.week_33_what_to_expect
      34 -> R.string.week_34_what_to_expect
      35 -> R.string.week_35_what_to_expect
      36 -> R.string.week_36_what_to_expect
      37 -> R.string.week_37_what_to_expect
      38 -> R.string.week_38_what_to_expect
      39 -> R.string.week_39_what_to_expect
      else -> R.string.week_40_what_to_expect
    }
  }