package org.dailyepisode.series

interface SeriesService {
  fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult
  fun lookup(remoteId: Int): SeriesLookupInfo?
  fun changesSinceYesterday(): SeriesChangedResult
}

data class SeriesSearchRequest(val query: String)

data class SeriesSearchResult(val results: List<SeriesSearchInfo>)

data class SeriesSearchInfo(
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double
)

data class SeriesLookupInfo(
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

data class SeriesChangedResult(val changedSeriesRemoteIds: List<Int>)