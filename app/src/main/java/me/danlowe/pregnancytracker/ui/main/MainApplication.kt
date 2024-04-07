package me.danlowe.pregnancytracker.ui.main

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.danlowe.pregnancytracker.CheckitemsQueries
import me.danlowe.pregnancytracker.ChecklistsQueries
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.di.MODULE_ALL_PREGNANCIES
import me.danlowe.pregnancytracker.di.MODULE_APP
import me.danlowe.pregnancytracker.di.MODULE_CHECKLIST
import me.danlowe.pregnancytracker.di.MODULE_CURRENT_WEEK
import me.danlowe.pregnancytracker.di.MODULE_LOGGING
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    initStrictMode()
    initKoin()
    initDatabases()
  }

  private fun initKoin() {
    startKoin {
      androidContext(this@MainApplication)
      modules(
        MODULE_APP,
        MODULE_ALL_PREGNANCIES,
        MODULE_CURRENT_WEEK,
        MODULE_LOGGING,
        MODULE_CHECKLIST,
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

  private fun initDatabases() {
    GlobalScope.launch(Dispatchers.IO) {
      val checkitemsQueries: CheckitemsQueries = get()
      val checklistsQueries: ChecklistsQueries = get()

      val count = checklistsQueries.getChecklistCount().executeAsOneOrNull()
      if (count != null && count > 0) return@launch

      checklistsQueries.apply {
        checkitemsQueries.apply {
          insert(1, getString(R.string.ci_research_birthing_methods), 0)
          insert(2, getString(R.string.ci_start_planning_nursery), 0)
          insert(3, getString(R.string.ci_create_savings_plan), 0)
          insert(4, getString(R.string.ci_research_birthing_facilities), 0)
          insert(5, getString(R.string.ci_interview_ob_midwives), 0)
          insert(6, getString(R.string.ci_discuss_birth_announcement), 0)
          insert(7, getString(R.string.ci_schedule_first_sonogram), 0)
          insert(8, getString(R.string.ci_start_looking_baby_names), 0)
        }
        insertCheckList(1, getString(R.string.cl_first_trimester), listOf(1, 2, 3, 4, 5, 6, 7, 8))
        checkitemsQueries.apply {
          insert(9, getString(R.string.ci_add_doctors_to_contacts), 0)
          insert(10, getString(R.string.ci_start_building_nursery), 0)
          insert(11, getString(R.string.ci_discuss_baby_names), 0)
          insert(12, getString(R.string.ci_research_diaper_types), 0)
          insert(13, getString(R.string.ci_research_car_seats), 0)
          insert(14, getString(R.string.ci_prepare_take_off_work_for_birth), 0)
          insert(15, getString(R.string.ci_deep_clean_home), 0)
        }
        insertCheckList(
          2,
          getString(R.string.cl_second_trimester),
          listOf(9, 10, 11, 12, 13, 14, 15),
        )
        checkitemsQueries.apply {
          insert(16, getString(R.string.ci_install_car_seat), 0)
          insert(17, getString(R.string.ci_prep_overnight_bag), 0)
          insert(18, getString(R.string.ci_practice_contraction_counter), 0)
          insert(19, getString(R.string.ci_finish_nursery), 0)
          insert(20, getString(R.string.ci_clear_space_for_camera), 0)
          insert(21, getString(R.string.ci_learn_route_to_birth_location), 0)
          insert(22, getString(R.string.ci_find_a_pet_sitter), 0)
        }
        checklistsQueries.insertCheckList(
          3,
          getString(R.string.cl_third_trimester),
          listOf(16, 17, 18, 19, 20, 21, 22),
        )
      }
    }
  }
}
