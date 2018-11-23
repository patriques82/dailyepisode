package org.dailyepisode.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun Date.toLocalDate(): LocalDate =
  toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

fun String.toLocalDate(): LocalDate =
  LocalDate.parse(this, DateTimeFormatter.ISO_DATE)