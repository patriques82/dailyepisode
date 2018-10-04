package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.SeriesSearchInfo
import org.dailyepisode.series.SeriesSearchResult

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SeriesSearchInfoDto(
  val remoteId: Int,
  val name: String,
  val overview: String?,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double
)

fun SeriesSearchInfo.toDto(): SeriesSearchInfoDto =
  SeriesSearchInfoDto(remoteId, name, overview, imageUrl, voteCount, voteAverage)

data class SeriesSearchResultDto(
  val results: List<SeriesSearchInfoDto>,
  val page: Int,
  val totalPages: Int,
  val totalResult: Int
)

fun SeriesSearchResult.toDto(): SeriesSearchResultDto =
  SeriesSearchResultDto(results.map { it.toDto() }, page, totalPages, totalResults)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SeriesLookupResultDto(
  val remoteId: Int,
  val name: String,
  val overview: String?,
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

fun SeriesLookupResult.toDto(): SeriesLookupResultDto =
  SeriesLookupResultDto(remoteId, name, overview, imageUrl, voteCount, voteAverage,
    firstAirDate, lastAirDate, genres, homepage, numberOfEpisodes, numberOfSeasons)