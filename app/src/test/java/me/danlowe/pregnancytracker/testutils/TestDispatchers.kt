package me.danlowe.pregnancytracker.testutils

import kotlinx.coroutines.CoroutineDispatcher
import me.danlowe.pregnancytracker.coroutines.AppDispatchers

class TestDispatchers(val testDispatcher: CoroutineDispatcher) : AppDispatchers {
  override val io: CoroutineDispatcher
    get() = testDispatcher
  override val default: CoroutineDispatcher
    get() = testDispatcher
  override val main: CoroutineDispatcher
    get() = testDispatcher
  override val unconfined: CoroutineDispatcher
    get() = testDispatcher
}
