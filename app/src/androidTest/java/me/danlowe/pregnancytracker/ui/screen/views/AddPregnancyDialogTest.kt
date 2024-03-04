package me.danlowe.pregnancytracker.ui.screen.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class AddPregnancyDialogTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun addPregnancyDialogContentIntegration() {
    composeTestRule.setContent {
      AddPregnancyDialogContent(
        addNewPregnancy = { _, _ -> },
      ) { }
    }

    composeTestRule.onNodeWithTag("addPregnancyDialogContent").assertIsDisplayed()


  }
}