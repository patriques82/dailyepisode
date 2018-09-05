package org.dailyepisode.subscription

interface SubscriptionService {
  fun createSubscription(subscriptionRequest: SubscriptionRequest, accountId: Long)
  fun findAll(): List<Subscription>
  fun findById(subscriptionId: Long): Subscription?
  fun deleteSubscription(subscriptionId: Long, accountId: Long)
}

data class SubscriptionRequest(
  val remoteId: Int,
  val name: String,
  val overview: String?,
  val imageUrl: String?
)

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