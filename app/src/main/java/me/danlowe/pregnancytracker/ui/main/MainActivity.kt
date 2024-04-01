package me.danlowe.pregnancytracker.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import kotlinx.coroutines.launch
import me.danlowe.pregnancytracker.handlers.mediapicker.MediaHandler
import me.danlowe.pregnancytracker.handlers.mediapicker.MediaRequestType
import me.danlowe.pregnancytracker.ui.screen.allpregnancies.AllPregnanciesScreen
import me.danlowe.pregnancytracker.ui.screen.currentweek.CurrentWeekTab
import me.danlowe.pregnancytracker.ui.screen.logscreen.tab.LogTab
import me.danlowe.pregnancytracker.ui.theme.PregnancyTrackerTheme
import me.danlowe.pregnancytracker.ui.views.FullScreenLoading
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModel()
  private val mediaHandler: MediaHandler by inject()

  private val pickMediaLauncher = registerForActivityResult(
    ActivityResultContracts.PickMultipleVisualMedia(),
  ) {
    mediaHandler.handleMedia(it)
  }

  private val takePictureLauncher = registerForActivityResult(
    ActivityResultContracts.TakePicture(),
  ) {
    mediaHandler.handleCamera(it)
  }

  @OptIn(KoinExperimentalAPI::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    lifecycleScope.launch {
      mediaHandler.requests.collect { request ->
        when (request) {
          is MediaRequestType.Camera -> {
            takePictureLauncher.launch(request.uri)
          }
          MediaRequestType.MediaPicker -> {
            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
          }
        }
      }
    }

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

              is MainState.HasExistingPregnancy -> {
                TabNavigator(
                  tab = CurrentWeekTab,
                  tabDisposable = {
                    TabDisposable(
                      navigator = it,
                      tabs = listOf(CurrentWeekTab, LogTab),
                    )
                  },
                ) { tabNavigator ->
                  Scaffold(
                    bottomBar = {
                      NavigationBar {
                      }
                      BottomAppBar(
                        actions = {
                          TabNavigationItem(
                            tab = CurrentWeekTab,
                            selected = tabNavigator.current == CurrentWeekTab,
                          ) {
                            tabNavigator.current = CurrentWeekTab
                          }
                          TabNavigationItem(
                            tab = LogTab,
                            selected = tabNavigator.current == LogTab,
                          ) {
                            tabNavigator.current = LogTab
                          }
                        },
                      )
                    },
                    content = {
                      Box(modifier = Modifier.padding(it)) {
                        CurrentTab()
                      }
                    },
                  )
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

@Composable
private fun RowScope.TabNavigationItem(tab: Tab, selected: Boolean, onClick: () -> Unit) {
  NavigationBarItem(
    selected = selected,
    onClick = onClick,
    label = { Text(tab.options.title) },
    icon = {
      Icon(
        painter = tab.options.icon ?: rememberVectorPainter(Icons.Default.Warning),
        contentDescription = tab.options.title,
      )
    },
  )
}
