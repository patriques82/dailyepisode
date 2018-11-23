package org.dailyepisode.series

import org.dailyepisode.subscription.SubscriptionUpdateRequest

interface RemoteSeriesServiceFacade {
  fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult
  fun lookup(remoteId: Int): SeriesLookupResult?
}

data class SeriesSearchRequest(
  val query: String,
  val page: Int
)

data class SeriesSearchResult(
  val results: List<SeriesSearchInfo>,
  val page: Int,
  val totalPages: Int,
  val totalResults: Int
)

data class SeriesSearchInfo(
  val remoteId: Int,
  val name: String,
  val overview: String?,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double
)

data class SeriesLookupResult(
  val remoteId: Int,
  val name: String,
  val overview: String?,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double,
  val firstAirDate: String?,
  val lastAirDate: String?,
  val lastAirDateIsNewSeason: Boolean?,
  val genres: List<String>,
  val homepage: String?,
  val numberOfEpisodes: Int,
  val numberOfSeasons: Int,
  val nextAirDate: String?,
  val nextAirDateIsNewSeason: Boolean?
)

fun SeriesLookupResult.toSeriesUpdatedLookupResult(): SeriesUpdatedLookupResult =
  SeriesUpdatedLookupResult(remoteId, imageUrl, lastAirDate, numberOfEpisodes, numberOfSeasons,
    nextAirDate, nextAirDateIsNewSeason)

data class SeriesUpdatedLookupResult(
  val remoteId: Int,
  val imageUrl: String?,
  val lastAirDate: String?,
  val numberOfEpisodes: Int,
  val numberOfSeasons: Int,
  val nextAirDate: String?,
  val nextAirDateIsNewSeason: Boolean?
)

fun SeriesUpdatedLookupResult.toSubscriptionUpdateRequest(): SubscriptionUpdateRequest =
  SubscriptionUpdateRequest(remoteId, imageUrl, lastAirDate, numberOfEpisodes, numberOfSeasons,
    nextAirDate, nextAirDateIsNewSeason)

data class UpdatedSeriesResult(val changedSeriesRemoteIds: List<Int>)

class SeriesLookupBatchService(val remoteSeriesServiceFacade: RemoteSeriesServiceFacade) {

  fun lookup(remoteIds: List<Int>): List<SeriesLookupResult> {
    val lookups = mutableListOf<SeriesLookupResult>()
    remoteIds.forEach {
      val seriesLookupResult: SeriesLookupResult? = remoteSeriesServiceFacade.lookup(it)
      if (seriesLookupResult == null) {
        throw SeriesNotFoundException("ID '$it' not found")
      }
      lookups += seriesLookupResult
    }
    return lookups
  }

}

class SeriesNotFoundException(message: String) : RuntimeException(message)