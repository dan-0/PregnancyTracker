package me.danlowe.pregnancytracker.ui.screen.currentweek.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.danlowe.models.TrimesterProgress
import me.danlowe.pregnancytracker.ui.screen.currentweek.TrimesterProgressBar

@Composable
fun TrimesterProgressView(trimesterProgress: TrimesterProgress) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Text("Trimester Progress")
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      TrimesterProgressBar(
        modifier = Modifier.weight(1f),
        currentProgress = trimesterProgress.firstTrimester,
      )
      TrimesterProgressBar(
        modifier = Modifier.weight(1f),
        currentProgress = trimesterProgress.secondTrimester,
      )
      TrimesterProgressBar(
        modifier = Modifier.weight(1f),
        currentProgress = trimesterProgress.thirdTrimester,
      )
    }
  }
}