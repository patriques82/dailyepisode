package org.dailyepisode.subscription

import org.dailyepisode.series.SeriesLookupInfo
import org.dailyepisode.series.SeriesService

interface SubscriptionService {
  fun createSubscription(remoteIds: List<Int>, accountId: Long)
  fun findAll(): List<Subscription>
  fun findById(subscriptionId: Long): Subscription?
  fun findByRemoteId(remoteId: Int): Subscription?
  fun deleteSubscription(subscriptionId: Long, accountId: Long)
}

data class Subscription(
  val id: Long?,
  val remoteId: Int,
  val name: String,
  val overview: String?,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double,
  val firstAirDate: String?,
  val lastAirDate: String?,
  val genres: List<String>,
  val homepage: String?,
  val numberOfEpisodes: Int,
  val numberOfSeasons: Int
)

typealias PartialSubscriptionResult = Pair<List<Int>, List<Subscription>>

class SubscriptionLoadService(val subscriptionService: SubscriptionService) {

  fun load(remoteIds: List<Int>): PartialSubscriptionResult {
    val notFound = mutableListOf<Int>()
    val storedSubscriptions = mutableListOf<Subscription>()
    remoteIds.forEach {
      val subscription: Subscription? = subscriptionService.findByRemoteId(it)
      if (subscription != null) {
        storedSubscriptions += subscription
      } else {
        notFound += it
      }
    }
    return notFound to storedSubscriptions
  }

}

class SubscriptionFetchService(val seriesService: SeriesService) {

  fun fetch(remoteIds: List<Int>): PartialSubscriptionResult {
    val notFound = mutableListOf<Int>()
    val remoteSubscriptions = mutableListOf<Subscription>()
    remoteIds.forEach {
      val seriesLookupInfo: SeriesLookupInfo? = seriesService.lookup(it)
      if (seriesLookupInfo != null) {
        remoteSubscriptions += seriesLookupInfo.toSubscription()
      } else {
        notFound += it
      }
    }
    return notFound to remoteSubscriptions
  }

  private fun SeriesLookupInfo.toSubscription(): Subscription =
    Subscription(null, remoteId, name, overview, imageUrl, voteCount, voteAverage,
      firstAirDate, lastAirDate, genres, homepage, numberOfEpisodes, numberOfSeasons)

}

class SubscriptionRemoteIdNullPointerException(message: String) : RuntimeException(message)