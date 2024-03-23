package me.danlowe.pregnancytracker.ui.screen.allpregnancies.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.models.UiPregnancy

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PregnancyItem(
  pregnancy: UiPregnancy,
  removeClicked: (Long) -> Unit,
  modifier: Modifier = Modifier,
  clicked: (Long) -> Unit,
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(16.dp)
      .combinedClickable(
        onLongClickLabel = stringResource(R.string.remove_pregnancy),
        onLongClick = { removeClicked(pregnancy.id) },
        onClickLabel = stringResource(R.string.view_pregnancy_details),
        onClick = { clicked(pregnancy.id) },
      )
      .testTag("pregnancyCard${pregnancy.id}"),
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        painter = painterResource(id = R.drawable.ic_baby_in_womb),
        // not important
        contentDescription = null,
        modifier = Modifier
          .size(100.dp)
          .testTag("fetusImage${pregnancy.id}"),
        contentScale = ContentScale.Crop,
      )

      Column(
        modifier = Modifier
          .weight(1f),
        verticalArrangement = Arrangement.Center,
      ) {
        Text(
          text = stringResource(R.string.pregnancy_with, pregnancy.motherName),
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier.testTag("motherName${pregnancy.id}"),
        )

        Text(
          text = stringResource(R.string.due_date_with_date, pregnancy.dueDate),
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier.testTag("dueDate${pregnancy.id}"),
        )
      }
    }
  }
}
