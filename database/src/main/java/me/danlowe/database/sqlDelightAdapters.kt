package me.danlowe.database

import app.cash.sqldelight.ColumnAdapter

object ListOfIntDbAdapter : ColumnAdapter<List<Int>, String> {
  override fun decode(databaseValue: String): List<Int> {
    return if (databaseValue.isEmpty()) {
      return emptyList()
    } else {
      databaseValue.split(",").map { it.toInt() }
    }
  }

  override fun encode(value: List<Int>): String {
    return value.joinToString(",")
  }
}
