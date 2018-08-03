package org.dailyepisode.search

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
internal class ImdbSeriesInfo(
  val id: Int,
  val name: String,
  val overview: String,
  val poster_path: String?,
  val vote_count: Int,
  val vote_average: Double
)