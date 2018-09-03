package org.dailyepisode.series

interface SeriesSearchService {
  fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult
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
