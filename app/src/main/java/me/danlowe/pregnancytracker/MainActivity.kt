package me.danlowe.pregnancytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import me.danlowe.pregnancytracker.ui.screen.allpregnancies.AllPregnanciesScreen
import me.danlowe.pregnancytracker.ui.screen.currentweek.CurrentWeekScreen
import me.danlowe.pregnancytracker.ui.theme.PregnancyTrackerTheme
import me.danlowe.pregnancytracker.ui.views.FullScreenLoading
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModel()

  @OptIn(KoinExperimentalAPI::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      PregnancyTrackerTheme {
        KoinAndroidContext {
          // A surface container using the 'background' color from the theme
          Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val state = viewModel.state.collectAsState()
            when (val currentState = state.value) {
              MainState.Loading -> {
                FullScreenLoading()
              }
              is MainState.AddPregnancy -> {
                Navigator(CurrentWeekScreen(currentState.pregnancyId)) { navigator ->
                  SlideTransition(navigator)
                }
              }
              MainState.AllPregnancies -> {
                Navigator(AllPregnanciesScreen()) { navigator ->
                  SlideTransition(navigator)
                }
              }
            }
          }
        }
      }
    }
  }
}

