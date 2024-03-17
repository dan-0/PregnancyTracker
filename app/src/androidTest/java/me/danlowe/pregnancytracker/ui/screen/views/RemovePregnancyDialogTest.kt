package me.danlowe.pregnancytracker.ui.screen.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.testutils.targetContext
import me.danlowe.pregnancytracker.ui.screen.home.views.RemovePregnancyDialogContent
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RemovePregnancyDialogTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun removePregnancyDialogDisplayedCorrectly() {
    composeRule.setContent {
      RemovePregnancyDialogContent(
        modifier = Modifier.fillMaxWidth(),
        removePregnancy = { },
        onDismiss = { },
      )
    }

    composeRule.onNodeWithTag("removePregnancyDialogTitle")
      .assertIsDisplayed()
      .assertTextEquals(targetContext.getString(R.string.please_confirm))

    composeRule.onNodeWithTag("removePregnancyDialogPrompt")
      .assertIsDisplayed()
      .assertTextEquals(targetContext.getString(R.string.confirm_delete_pregnancy))

    composeRule.onNodeWithTag("cancelButton")
      .assertIsDisplayed()
      .assertTextEquals(targetContext.getString(R.string.cancel))
      .assertIsEnabled()

    composeRule.onNodeWithTag("deleteButton")
      .assertIsDisplayed()
      .assertTextEquals(targetContext.getString(R.string.confirm))
      .assertIsEnabled()
  }

  @Test
  fun removePregnancyDialogButtons() {
    val expectedButtonEvents = listOf(
      // From cancel button
      "dismissed",
      // From confirm button
      "removed",
      "dismissed",
    )
    val buttonEvents = mutableListOf<String>()

    composeRule.setContent {
      RemovePregnancyDialogContent(
        modifier = Modifier.fillMaxWidth(),
        removePregnancy = {
          buttonEvents.add("removed")
        },
        onDismiss = {
          buttonEvents.add("dismissed")
        },
      )
    }

    composeRule.onNodeWithTag("cancelButton").performClick()
    assertEquals(expectedButtonEvents.subList(0, 1), buttonEvents)

    composeRule.onNodeWithTag("deleteButton").performClick()
    assertEquals(expectedButtonEvents, buttonEvents)
  }
}
