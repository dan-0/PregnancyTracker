package me.danlowe.pregnancytracker.ui.screen.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.util.date.fromUtcLongToLocalDateInstant
import me.danlowe.pregnancytracker.util.date.toLocalizedShortDate

@Composable
fun AddPregnancyDialog(
  closeDialog: () -> Unit,
  addNewPregnancy: (String, Long) -> Unit,
) {
  Dialog(
    onDismissRequest = closeDialog,
  ) {
    AddPregnancyDialogContent(addNewPregnancy = addNewPregnancy, closeDialog = closeDialog)
  }
}

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
fun AddPregnancyDialogContent(
  addNewPregnancy: (String, Long) -> Unit,
  modifier: Modifier = Modifier,
  closeDialog: () -> Unit,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
      .padding(16.dp)
      .testTag("addPregnancyDialogContent"),
    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
  ) {
    var motherName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
      mutableStateOf(TextFieldValue())
    }

    val datePickerState =
      rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
      )

    val coroutineScope = rememberCoroutineScope()
    Text(
      stringResource(R.string.add_a_new_pregnancy),
      style = MaterialTheme.typography.titleLarge,
    )

    val isNextEnabled = remember(motherName, datePickerState) {
      fun(currentPage: Int): Boolean {
        return when (currentPage) {
          0 -> motherName.text.isNotBlank()
          1 -> datePickerState.selectedDateMillis != null
          2 -> motherName.text.isNotBlank() && datePickerState.selectedDateMillis != null
          else -> false
        }
      }
    }

    val pagerState = rememberPagerState(pageCount = { 3 })
    HorizontalPager(
      state = pagerState,
      userScrollEnabled = false,
      beyondBoundsPageCount = 2,
    ) { page ->
      PagerContent(
        page = page,
        motherName = motherName,
        datePickerState = datePickerState,
        isNextEnabled = isNextEnabled(page),
        goToNext = {
          coroutineScope.launch {
            pagerState.animateScrollToPage(page + 1)
          }
        },
      ) { newMotherName ->
        motherName = newMotherName
      }
    }
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      // Back
      PagerBackButton(pagerState, coroutineScope)
      // Page indicator
      PageIndicator(pagerState)
      // Next
      PagerNextButton(pagerState, coroutineScope, isNextEnabled) {
        addNewPregnancy(motherName.text, datePickerState.selectedDateMillis!!)
        closeDialog()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerContent(
  page: Int,
  motherName: TextFieldValue,
  datePickerState: DatePickerState,
  modifier: Modifier = Modifier,
  isNextEnabled: Boolean,
  goToNext: () -> Unit,
  nameUpdated: (TextFieldValue) -> Unit,
) {
  when (page) {
    0 -> {
      var isError by rememberSaveable(motherName) {
        mutableStateOf(false)
      }
      OutlinedTextField(
        value = motherName,
        onValueChange = { newName -> nameUpdated(newName) },
        label = { Text(stringResource(R.string.mother_s_name)) },
        placeholder = { Text(stringResource(R.string.mother_s_name)) },
        supportingText = {
          if (isError) {
            Text(
              "Must not be empty",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.error,
            )
          }
        },
        keyboardOptions = KeyboardOptions(
          capitalization = KeyboardCapitalization.Words,
          imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            if (isNextEnabled) {
              goToNext()
            } else {
              isError = true
            }
          },
        ),
        isError = isError,
        modifier = modifier.fillMaxWidth().testTag("motherNameTextField"),
      )
    }

    1 -> {
      DatePicker(
        state = datePickerState,
        headline = { Text(stringResource(R.string.due_date)) },
        title = null,
        showModeToggle = false,
        modifier = modifier.fillMaxWidth().testTag("datePicker"),
      )
    }

    2 -> {
      // confirm
      Column(
        modifier = modifier.fillMaxWidth().testTag("confirmationScreen"),
      ) {
        Text(
          stringResource(R.string.confirm_your_information),
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.padding(bottom = 8.dp),
        )
        Text(
          stringResource(id = R.string.mother_s_name),
          style = MaterialTheme.typography.labelSmall,
        )
        Text(
          motherName.text,
          modifier = Modifier.padding(start = 8.dp),
        )
        Text(
          stringResource(id = R.string.due_date),
          style = MaterialTheme.typography.labelSmall,
        )
        Text(
          datePickerState.selectedDateMillis
            ?.fromUtcLongToLocalDateInstant()
            ?.toLocalizedShortDate()
            .toString(),
          modifier = Modifier.padding(start = 8.dp),
        )
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowScope.PagerNextButton(
  pagerState: PagerState,
  coroutineScope: CoroutineScope,
  isNextEnabled: (Int) -> Boolean,
  modifier: Modifier = Modifier,
  done: () -> Unit,
) {
  Box(
    modifier = modifier.weight(1f),
    contentAlignment = Alignment.CenterEnd,
  ) {
    val currentPage = pagerState.currentPage
    val isLastPage = currentPage == pagerState.pageCount - 1
    Button(
      onClick = {
        if (isLastPage) {
          done()
        } else {
          coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
          }
        }
      },
      enabled = isNextEnabled(currentPage),
      modifier = Modifier.testTag("nextButton"),
    ) {
      val text =
        if (isLastPage) {
          stringResource(R.string.done)
        } else {
          stringResource(R.string.next)
        }
      Text(text)
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowScope.PageIndicator(pagerState: PagerState, modifier: Modifier = Modifier) {
  Row(
    modifier = modifier.weight(1f),
    horizontalArrangement = Arrangement.Center,
  ) {
    val currentPage = pagerState.currentPage
    repeat(pagerState.pageCount) { currentPageIndicatorPosition ->
      Box(
        modifier = Modifier
          .padding(2.dp)
          .clip(CircleShape)
          .background(
            color = if (currentPage == currentPageIndicatorPosition) {
              MaterialTheme.colorScheme.primary
            } else {
              MaterialTheme.colorScheme.onSurface
            },
          )
          .size(8.dp),
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowScope.PagerBackButton(
  pagerState: PagerState,
  coroutineScope: CoroutineScope,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.weight(1f),
    contentAlignment = Alignment.CenterStart,
  ) {
    val currentPage = pagerState.currentPage
    if (currentPage > 0) {
      Button(
        onClick = {
          coroutineScope.launch {
            pagerState.animateScrollToPage(currentPage - 1)
          }
        },
      ) {
        Text(stringResource(R.string.back))
      }
    }
  }
}
