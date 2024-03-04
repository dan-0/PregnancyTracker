package me.danlowe.pregnancytracker

import android.app.Application
import me.danlowe.pregnancytracker.di.MODULE_APP
import me.danlowe.pregnancytracker.di.MODULE_HOME
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@MainApplication)
      modules(MODULE_APP, MODULE_HOME)
    }
  }
}
