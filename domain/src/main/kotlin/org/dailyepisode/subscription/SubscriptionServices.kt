package org.dailyepisode.subscription

interface SubscriptionStorageService {
  fun createSubscriptions(createSubscriptionsRequest: CreateSubscriptionsRequest)
  fun findAll(): List<Subscription>
  fun findById(subscriptionId: Long): Subscription?
  fun findByRemoteId(remoteId: Int): Subscription?
  fun deleteSubscription(subscriptionId: DeleteSubscriptionRequest)
}

data class CreateSubscriptionsRequest(
  val accountId: Long,
  val remoteIds: List<Int>
)

data class DeleteSubscriptionRequest(
  val accountId: Long,
  val subscriptionId: Long
)

data class Subscription(
  val id: Long,
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

class SubscriptionBatchService(val subscriptionStorageService: SubscriptionStorageService) {

  fun findByRemoteIds(remoteIds: List<Int>): PartialSubscriptionResult {
    val notFound = mutableListOf<Int>()
    val storedSubscriptions = mutableListOf<Subscription>()
    remoteIds.forEach {
      val subscription: Subscription? = subscriptionStorageService.findByRemoteId(it)
      if (subscription != null) {
        storedSubscriptions += subscription
      } else {
        notFound += it
      }
    }
    return notFound to storedSubscriptions
  }

}