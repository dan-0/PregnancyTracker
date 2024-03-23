package me.danlowe.models

import kotlinx.collections.immutable.ImmutableList

data class LogEntry(
  val id: Long,
  val attachmentUris: ImmutableList<String>,
  val date: Long,
  val entry: String,
)