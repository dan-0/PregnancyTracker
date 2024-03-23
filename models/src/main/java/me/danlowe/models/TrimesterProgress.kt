package me.danlowe.models

data class TrimesterProgress(
  val week: Int
) {
  val firstTrimester: Float = when (week) {
    in 1..13 -> week.toFloat() / 13
    else -> 1f
  }
  val secondTrimester: Float = when (week) {
    in 1..13 -> 0f
    in 14..26 -> (week - 13) / 13f
    else -> 1f
  }
  val thirdTrimester: Float = when (week) {
    in 1..26 -> 0f
    else -> (week - 26) / 13f
  }
  val totalProgress = week.toFloat() / 40
}