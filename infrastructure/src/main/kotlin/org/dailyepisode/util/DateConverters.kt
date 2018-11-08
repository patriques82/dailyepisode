package org.dailyepisode.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Date.toLocalDateTime(): LocalDateTime =
  toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()