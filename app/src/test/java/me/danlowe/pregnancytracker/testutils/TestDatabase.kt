package me.danlowe.pregnancytracker.testutils

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import me.danlowe.pregnancytracker.PregnancyTracker

class TestDatabase {
  private val database = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
  val memoryPregnancyTracker = PregnancyTracker.Schema.create(database).let {
    PregnancyTracker(database)
  }
}
