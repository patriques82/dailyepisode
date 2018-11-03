package org.dailyepisode.subscription

import java.time.LocalDateTime

interface SubscriptionStorageService {
  fun createSubscriptions(subscriptionCreateRequest: SubscriptionCreateRequest): Subscription?
  fun findAll(): List<Subscription>
  fun findById(subscriptionId: Long): Subscription?
  fun findByRemoteId(remoteId: Int): Subscription?
  fun deleteSubscription(subscriptionDeleteRequest: SubscriptionDeleteRequest)
  fun update(subscriptionUpdateRequest: SubscriptionUpdateRequest)
}

data class SubscriptionCreateRequest(
  val accountId: Long,
  val remoteId: Int
)

data class SubscriptionDeleteRequest(
  val accountId: Long,
  val subscriptionId: Long
)

data class SubscriptionUpdateRequest(
  val remoteId: Int,
  val numberOfSeasons: Int
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
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
)