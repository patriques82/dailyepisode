package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.dailyepisode.series.SeriesLookupInfo
import org.dailyepisode.series.SeriesSearchInfo

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SeriesSearchInfoDto(
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double
)

fun SeriesSearchInfo.toDto(): SeriesSearchInfoDto =
  SeriesSearchInfoDto(remoteId, name, overview, imageUrl, voteCount, voteAverage)

data class SeriesSearchResultDto(val results: List<SeriesSearchInfoDto>)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SeriesLookupInfoDto(
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double,
  val firstAirDate: String,
  val lastAirDate: String,
  val genres: List<String>,
  val homepage: String?,
  val numberOfEpisodes: Int,
  val numberOfSeasons: Int
)

fun SeriesLookupInfo.toDto(): SeriesLookupInfoDto =
  SeriesLookupInfoDto(remoteId, name, overview, imageUrl, voteCount, voteAverage,
    firstAirDate, lastAirDate, genres, homepage, numberOfEpisodes, numberOfSeasons)