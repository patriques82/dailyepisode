package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.dailyepisode.search.SeriesInfo

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SeriesInfoDto(
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double
)

fun SeriesInfo.toDto(): SeriesInfoDto =
  SeriesInfoDto(remoteId, name, overview, imageUrl, voteCount, voteAverage)
