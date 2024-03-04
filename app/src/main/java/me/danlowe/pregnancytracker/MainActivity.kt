package me.danlowe.pregnancytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import me.danlowe.pregnancytracker.ui.screen.HomeScreen
import me.danlowe.pregnancytracker.ui.theme.PregnancyTrackerTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      PregnancyTrackerTheme {
        KoinAndroidContext {
          // A surface container using the 'background' color from the theme
          Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Navigator(HomeScreen()) { navigator ->
              SlideTransition(navigator)
            }
          }
        }
      }
    }
  }
}