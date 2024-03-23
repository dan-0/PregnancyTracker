package me.danlowe.pregnancytracker

import android.app.Application
import me.danlowe.pregnancytracker.di.MODULE_ALL_PREGNANCIES
import me.danlowe.pregnancytracker.di.MODULE_APP
import me.danlowe.pregnancytracker.di.MODULE_CURRENT_WEEK
import me.danlowe.pregnancytracker.di.MODULE_LOGGING
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@MainApplication)
      modules(
        MODULE_APP,
        MODULE_ALL_PREGNANCIES,
        MODULE_CURRENT_WEEK,
        MODULE_LOGGING,
      )
    }
  }
}
