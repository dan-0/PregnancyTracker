package me.danlowe.pregnancytracker.ui.screen.views

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.testutils.targetContext
import me.danlowe.pregnancytracker.ui.screen.allpregnancies.views.AddPregnancyDialogContent
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

class AddPregnancyDialogTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun addPregnancyDialogContentIntegration() {
    // Mother name in test plus the test date in UTC millis
    val expectedSubmissions = listOf("testName 1609459200000")
    val submissions = mutableListOf<String>()
    composeTestRule.setContent {
      AddPregnancyDialogContent(
        addNewPregnancy = { name, date ->
          submissions.add("$name $date")
        },
      ) { }
    }

    composeTestRule.onNodeWithTag("addPregnancyDialogContent").assertIsDisplayed()
    val motherNameField = composeTestRule.onNodeWithTag("motherNameTextField")
    val nextButton = composeTestRule.onNodeWithTag("nextButton")
    val backButton = composeTestRule.onNodeWithTag("backButton")
    val datePicker = composeTestRule.onNodeWithTag("datePicker")

    // initial page
    // Given
    verifyTitleDisplayed()
    verifyBackButtonState(0)
    verifyNextButtonState(0, false)
    verifyPageIndicatorState(0)

    motherNameField
      .assertIsDisplayed()
      .assertTextEquals(
        targetContext.getString(R.string.mother_s_name),
        includeEditableText = false,
      )

    // When
    motherNameField.performTextInput("testName")

    // Then
    verifyNextButtonState(0, true)
    nextButton.performClick()

    // date page
    // Given
    verifyTitleDisplayed()
    verifyBackButtonState(1)
    verifyNextButtonState(1, false)
    verifyPageIndicatorState(1)

    // When
    datePicker
      .performClick()

    // DatePicker is kinda new so test support doesn't support direct text replacement yet
    val keys = listOf(
      Key.Zero,
      Key.One,
      Key.Zero,
      Key.One,
      Key.Two,
      Key.Zero,
      Key.Two,
      Key.One,
    )
    datePicker.performKeyInput {
      keys.forEach { key ->
        pressKey(key)
      }
    }

    composeTestRule.onNodeWithText("01/01/2021")
      .assertIsDisplayed()

    // Then
    verifyNextButtonState(1, true)
    nextButton.performClick()

    // confirmation page
    // Given
    verifyTitleDisplayed()
    verifyBackButtonState(2)
    verifyNextButtonState(2, true)
    verifyPageIndicatorState(2)

    // Then
    verifyConfirmationPage()

    // Back button goes back
    backButton.performClick()
    // fast path, just verify the page indicator
    verifyPageIndicatorState(1)
    backButton.performClick()
    verifyPageIndicatorState(0)

    // Go back to the confirmation page
    nextButton.performClick()
    verifyPageIndicatorState(1)
    nextButton.performClick()
    verifyPageIndicatorState(2)
    verifyConfirmationPage()

    // Next button submits
    nextButton.performClick()

    assertEquals(expectedSubmissions, submissions)
  }

  private fun verifyConfirmationPage() {
    composeTestRule.onNodeWithTag("confirmationScreen")
      .assertIsDisplayed()

    composeTestRule.onNodeWithTag("confirmationTitle")
      .assertIsDisplayed()
      .assertTextEquals(targetContext.getString(R.string.confirm_your_information))

    composeTestRule.onNodeWithTag("motherNameLabel")
      .assertIsDisplayed()
      .assertTextEquals(targetContext.getString(R.string.mother_s_name))

    composeTestRule.onNodeWithTag("motherNameValue")
      .assertIsDisplayed()
      .assertTextEquals("testName")

    composeTestRule.onNodeWithTag("dueDateLabel")
      .assertIsDisplayed()
      .assertTextEquals(targetContext.getString(R.string.due_date))

    // display text is subject to localization, so we'll just check its displayed and verify the
    // date post submit
    composeTestRule.onNodeWithTag("dueDateValue")
      .assertIsDisplayed()
  }

  private fun verifyTitleDisplayed() {
    composeTestRule.onNodeWithTag("addPregnancyDialogTitle")
      .assertIsDisplayed()
      .assertTextEquals(targetContext.getString(R.string.add_a_new_pregnancy))
  }

  private fun verifyPageIndicatorState(expectedPage: Int) {
    val pageOneTag = "pageIndicator0-${expectedPage == 0}"
    val pageTwoTag = "pageIndicator1-${expectedPage == 1}"
    val pageThreeTag = "pageIndicator2-${expectedPage == 2}"
    composeTestRule.onNodeWithTag(pageOneTag)
      .assertIsDisplayed()
    composeTestRule.onNodeWithTag(pageTwoTag)
      .assertIsDisplayed()
    composeTestRule.onNodeWithTag(pageThreeTag)
      .assertIsDisplayed()
  }

  private fun verifyBackButtonState(expectedPage: Int) {
    composeTestRule.onNodeWithTag("backButton")
      .apply {
        when (expectedPage) {
          0 -> assertDoesNotExist()
          1 -> assertIsDisplayed().assertTextEquals(targetContext.getString(R.string.back))
          2 -> assertIsDisplayed().assertTextEquals(targetContext.getString(R.string.back))
          else -> fail("Invalid page: $expectedPage")
        }
      }
  }

  private fun verifyNextButtonState(expectedPage: Int, isNextEnabled: Boolean) {
    composeTestRule.onNodeWithTag("nextButton")
      .apply {
        when (expectedPage) {
          0 -> assertIsDisplayed().assertTextEquals(targetContext.getString(R.string.next))
          1 -> assertIsDisplayed().assertTextEquals(targetContext.getString(R.string.next))
          2 -> assertIsDisplayed().assertTextEquals(targetContext.getString(R.string.done))
          else -> fail("Invalid page: $expectedPage")
        }

        if (isNextEnabled) {
          assertIsEnabled()
        } else {
          assertIsNotEnabled()
        }
      }
  }
}
