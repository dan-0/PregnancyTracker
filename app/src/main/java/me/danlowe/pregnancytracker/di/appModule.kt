package me.danlowe.pregnancytracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import me.danlowe.pregnancytracker.PregnancyTracker
import me.danlowe.pregnancytracker.handlers.attachment.AttachmentHandler
import me.danlowe.pregnancytracker.handlers.attachment.RealAttachmentHandler
import me.danlowe.pregnancytracker.handlers.mediapicker.MediaHandler
import me.danlowe.pregnancytracker.repo.log.LogRepo
import me.danlowe.pregnancytracker.repo.log.RealLogRepo
import me.danlowe.pregnancytracker.ui.main.MainViewModel
import me.danlowe.pregnancytracker.ui.screen.allpregnancies.AllPregnanciesScreenModel
import me.danlowe.pregnancytracker.ui.screen.allpregnancies.RealAllPregnanciesScreenModel
import me.danlowe.pregnancytracker.ui.screen.currentweek.CurrentWeekScreenModel
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.AddLogModel
import me.danlowe.pregnancytracker.ui.screen.logscreen.edit.EditLogScreenModel
import me.danlowe.pregnancytracker.ui.screen.logscreen.entires.LogEntriesModel
import me.danlowe.utils.coroutines.AppDispatchers
import me.danlowe.utils.coroutines.RealAppDispatchers
import me.danlowe.utils.date.AppDateFormatter
import me.danlowe.utils.date.AppTime
import me.danlowe.utils.date.RealAppDateFormatter
import me.danlowe.utils.date.RealAppTime
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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
    val database: PregnancyTracker = get()
    database.logsQueries
  }

  single {
    androidContext().dataStore
  }

  singleOf(::RealLogRepo).bind(LogRepo::class)

  singleOf<AppDispatchers>(::RealAppDispatchers)

  singleOf(::RealAppDateFormatter).bind(AppDateFormatter::class)

  singleOf<AppTime>(::RealAppTime)

  viewModelOf(::MainViewModel)

  singleOf(::MediaHandler)

  singleOf(::RealAttachmentHandler).bind(AttachmentHandler::class)
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val MODULE_ALL_PREGNANCIES = module {
  factoryOf(::RealAllPregnanciesScreenModel).bind(AllPregnanciesScreenModel::class)
}

val MODULE_CURRENT_WEEK = module {
  factoryOf(::CurrentWeekScreenModel)
}

val MODULE_LOGGING = module {
  factoryOf(::LogEntriesModel)

  factoryOf(::AddLogModel)

  factoryOf(::EditLogScreenModel)
}
