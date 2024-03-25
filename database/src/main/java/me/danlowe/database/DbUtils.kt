package me.danlowe.database

object DbUtils {
  fun longToBoolean(long: Long): Boolean {
    return long == 1L
  }

  fun booleanToLong(boolean: Boolean): Long {
    return if (boolean) 1L else 0L
  }

  const val ID_NO_VALUE = -1L

  const val ATTACHMENT_SEPARATOR = ":::"
}
