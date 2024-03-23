package me.danlowe.pregnancytracker.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FullScreenLoading() {
  Column(
    modifier = Modifier.fillMaxSize(),
  ) {
    CircularProgressIndicator()
  }
}