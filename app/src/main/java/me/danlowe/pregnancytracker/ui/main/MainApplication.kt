package me.danlowe.pregnancytracker.ui.main

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import me.danlowe.pregnancytracker.di.MODULE_ALL_PREGNANCIES
import me.danlowe.pregnancytracker.di.MODULE_APP
import me.danlowe.pregnancytracker.di.MODULE_CURRENT_WEEK
import me.danlowe.pregnancytracker.di.MODULE_LOGGING
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    initStrictMode()
    initKoin()
  }

  private fun initKoin() {
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

  private fun initStrictMode() {
    StrictMode.setThreadPolicy(
      ThreadPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build(),
    )
    StrictMode.setVmPolicy(
      VmPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .penaltyDeath()
          .build(),
    )
  }
}
