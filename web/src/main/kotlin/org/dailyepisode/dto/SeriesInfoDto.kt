package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class SeriesInfoDto(
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double
)
