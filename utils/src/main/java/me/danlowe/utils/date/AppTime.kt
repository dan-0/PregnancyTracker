package me.danlowe.utils.date

import java.time.Instant

interface AppTime {
  fun currentUtcTimeAsString(): String
}

class RealAppTime : AppTime {
  override fun currentUtcTimeAsString(): String {
    return Instant.now().to3339String()
  }
}
