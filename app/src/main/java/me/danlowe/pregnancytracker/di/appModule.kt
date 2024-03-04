package me.danlowe.pregnancytracker.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import me.danlowe.pregnancytracker.PregnancyTracker
import me.danlowe.pregnancytracker.coroutines.AppDispatchers
import me.danlowe.pregnancytracker.coroutines.RealAppDispatchers
import me.danlowe.pregnancytracker.ui.screen.HomeScreenModel
import me.danlowe.pregnancytracker.util.date.AppDateFormatter
import me.danlowe.pregnancytracker.util.date.RealAppDateFormatter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val MODULE_APP = module {
  single {
    AndroidSqliteDriver(PregnancyTracker.Schema, androidContext(), "app.db") as SqlDriver
  }

  single {
    PregnancyTracker(get())
  }

  single {
    val database: PregnancyTracker = get()
    database.pregnancyQueries
  }

  single {
    RealAppDispatchers() as AppDispatchers
  }

  single {
    RealAppDateFormatter(androidContext()) as AppDateFormatter
  }
}

val MODULE_HOME = module {
  factory {
    HomeScreenModel(get(), get(), get())
  }
}
