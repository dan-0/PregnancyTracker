package me.danlowe.pregnancytracker.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import me.danlowe.pregnancytracker.MainViewModel
import me.danlowe.pregnancytracker.PregnancyTracker
import me.danlowe.pregnancytracker.ui.screen.allpregnancies.RealAllPregnanciesScreenModel
import me.danlowe.pregnancytracker.ui.screen.currentweek.CurrentWeekScreenModel
import me.danlowe.utils.coroutines.RealAppDispatchers
import me.danlowe.utils.date.RealAppDateFormatter
import me.danlowe.utils.date.RealAppTime
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
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

  singleOf(::RealAppDispatchers)

  singleOf(::RealAppDateFormatter)

  singleOf(::RealAppTime)

  viewModelOf(::MainViewModel)
}

val MODULE_HOME = module {
  factoryOf(::RealAllPregnanciesScreenModel)
}

val MODULE_CURRENT_WEEK = module {
  factoryOf(::CurrentWeekScreenModel)
}
