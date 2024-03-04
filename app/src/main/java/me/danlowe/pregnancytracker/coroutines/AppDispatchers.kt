package me.danlowe.pregnancytracker.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatchers {
  val io: CoroutineDispatcher
  val default: CoroutineDispatcher
  val main: CoroutineDispatcher
  val unconfined: CoroutineDispatcher
}
