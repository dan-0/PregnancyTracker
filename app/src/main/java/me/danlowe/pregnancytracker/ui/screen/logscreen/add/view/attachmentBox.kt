package me.danlowe.pregnancytracker.ui.screen.logscreen.add.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AttachmentBox(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
  Box(
    modifier = modifier
      .size(AddLogDimens.attachmentSize)
      .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
      .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
      .clip(MaterialTheme.shapes.medium),
    contentAlignment = Alignment.Center,
  ) {
    content()
  }
}