package org.dailyepisode.series

interface RemoteSeriesServiceFacade {
  fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult
  fun lookupByRemoteId(remoteId: Int): SeriesLookupResult?
  fun updatesSinceYesterday(): SeriesUpdateResult
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

data class SeriesLookupResult(
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

data class SeriesUpdateResult(val changedSeriesRemoteIds: List<Int>)

class SeriesBatchService(val remoteSeriesServiceFacade: RemoteSeriesServiceFacade) {

  fun lookupByRemoteIds(remoteIds: List<Int>): List<SeriesLookupResult> {
    val lookups = mutableListOf<SeriesLookupResult>()
    remoteIds.forEach {
      val seriesLookupResult: SeriesLookupResult? = remoteSeriesServiceFacade.lookupByRemoteId(it)
      if (seriesLookupResult == null) {
        throw RemoteIdNullPointerException("Not found Id: $it")
      }
      lookups += seriesLookupResult
    }
    return lookups
  }

}

class RemoteIdNullPointerException(message: String) : RuntimeException(message)