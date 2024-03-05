package me.danlowe.pregnancytracker.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Before
import org.junit.Rule

class HomeScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Before
  fun setup() {
    // TODO need to create a DI module for test so we can test the injection of HomeScreenModel
  }
}
