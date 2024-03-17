package me.danlowe.pregnancytracker.ui.screen.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.models.UiPregnancy
import me.danlowe.pregnancytracker.testutils.targetContext
import me.danlowe.pregnancytracker.ui.screen.home.views.PregnancyItem
import org.junit.Rule
import org.junit.Test

class PregnancyItemTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun pregnancyItemDisplayedCorrectly() {
    composeRule.setContent {
      PregnancyItem(
        pregnancy = TEST_PREGNANCY,
        modifier = Modifier.fillMaxWidth(),
        removeClicked = { },
        clicked = { },
      )
    }

    composeRule.onNodeWithTag("pregnancyCard${TEST_PREGNANCY.id}")
      .assertIsDisplayed()

    composeRule.onNodeWithTag("fetusImage${TEST_PREGNANCY.id}", true)
      // Not rendered in test so
      .assertExists()

    composeRule.onNodeWithTag("motherName${TEST_PREGNANCY.id}", true)
      .assertIsDisplayed()
      .assertTextEquals(
        targetContext.getString(R.string.pregnancy_with, TEST_PREGNANCY.motherName),
      )

    composeRule.onNodeWithTag("dueDate${TEST_PREGNANCY.id}", true)
      .assertIsDisplayed()
      .assertTextEquals(
        targetContext.getString(R.string.due_date_with_date, TEST_PREGNANCY.dueDate),
      )
  }

  @Test
  fun pregnancyItemClicked() {
    var clicked = false
    composeRule.setContent {
      PregnancyItem(
        pregnancy = TEST_PREGNANCY,
        modifier = Modifier.fillMaxWidth(),
        removeClicked = { },
        clicked = { clicked = true },
      )
    }

    composeRule.onNodeWithTag("pregnancyCard${TEST_PREGNANCY.id}")
      .performClick()

    assert(clicked)
  }

  @Test
  fun pregnancyItemRemoveClicked() {
    var removeClicked = false
    composeRule.setContent {
      PregnancyItem(
        pregnancy = TEST_PREGNANCY,
        modifier = Modifier.fillMaxWidth(),
        removeClicked = { removeClicked = true },
        clicked = { },
      )
    }

    composeRule.onNodeWithTag("pregnancyCard${TEST_PREGNANCY.id}")
      .performTouchInput {
        longClick()
      }

    assert(removeClicked)
  }

  companion object {
    private val TEST_PREGNANCY = UiPregnancy(
      id = 3,
      dueDate = "test due date",
      motherName = "test mother's name",
    )
  }
}
