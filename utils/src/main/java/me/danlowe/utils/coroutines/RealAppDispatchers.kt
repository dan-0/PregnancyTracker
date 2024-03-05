package me.danlowe.utils.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RealAppDispatchers : AppDispatchers {
  override val io: CoroutineDispatcher = Dispatchers.IO
  override val default: CoroutineDispatcher = Dispatchers.Default
  override val main: CoroutineDispatcher = Dispatchers.Main
  override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}
