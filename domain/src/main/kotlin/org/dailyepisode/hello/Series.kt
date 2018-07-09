package org.dailyepisode.hello

sealed class Series
data class SeriesShort(val name: String,
                       val description: String,
                       val url: String,
                       val id: Long): Series()
data class SeriesFull(val name: String,
                      val description: String,
                      val url: String,
                      val id: Long): Series()
