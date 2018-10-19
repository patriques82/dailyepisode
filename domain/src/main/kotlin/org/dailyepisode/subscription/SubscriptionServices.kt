package org.dailyepisode.subscription

import java.util.*

interface SubscriptionStorageService {
  fun createSubscriptions(subscriptionCreateRequest: SubscriptionCreateRequest): Subscription?
  fun findAll(): List<Subscription>
  fun findById(subscriptionId: Long): Subscription?
  fun findByRemoteId(remoteId: Int): Subscription?
  fun deleteSubscription(subscriptionDeleteRequest: SubscriptionDeleteRequest)
}

data class SubscriptionCreateRequest(
  val accountId: Long,
  val remoteId: Int
)

data class SubscriptionDeleteRequest(
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
  val numberOfSeasons: Int,
  val createdAt: Date,
  val updatedAt: Date
)