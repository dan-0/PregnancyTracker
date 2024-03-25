package me.danlowe.models

import kotlinx.collections.immutable.ImmutableList

data class LogEntry(
  val id: Long,
  val pregnancyId: Long,
  val attachmentUris: ImmutableList<String>,
  val date: String,
  val updatedDate: String?,
  val entry: String,
)
