package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.dailyepisode.series.SeriesSearchInfo

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SeriesInfoDto(
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double
)

fun SeriesSearchInfo.toDto(): SeriesInfoDto =
  SeriesInfoDto(remoteId, name, overview, imageUrl, voteCount, voteAverage)

data class SeriesSearchResultDto(val results: List<SeriesInfoDto>)