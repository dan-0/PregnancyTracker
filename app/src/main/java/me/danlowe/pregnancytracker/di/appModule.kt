package me.danlowe.pregnancytracker.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import me.danlowe.pregnancytracker.PregnancyTracker
import me.danlowe.pregnancytracker.ui.screen.home.HomeScreenModel
import me.danlowe.pregnancytracker.ui.screen.home.RealHomeScreenModel
import me.danlowe.utils.coroutines.AppDispatchers
import me.danlowe.utils.coroutines.RealAppDispatchers
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
    me.danlowe.utils.date.RealAppDateFormatter(androidContext()) as me.danlowe.utils.date.AppDateFormatter
  }
}

val MODULE_HOME = module {
  factory {
    RealHomeScreenModel(get(), get(), get()) as HomeScreenModel
  }
}
