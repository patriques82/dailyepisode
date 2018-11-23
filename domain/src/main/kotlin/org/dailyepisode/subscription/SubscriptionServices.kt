package org.dailyepisode.subscription

import java.time.LocalDate

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
  val imageUrl: String?,
  val lastAirDate: String?,
  val numberOfEpisodes: Int,
  val numberOfSeasons: Int,
  val nextAirDate: String?,
  val nextAirDateIsNewSeason: Boolean?
)

data class Subscription(
  val id: Long,
  val remoteId: Int,
  val name: String,
  val overview: String?,
  val imageUrl: String?,
  val voteCount: Int,
  val voteAverage: Double,
  val firstAirDate: LocalDate?,
  val lastAirDate: LocalDate?,
  val lastAirDateIsNewSeason: Boolean?,
  val genres: List<String>,
  val homepage: String?,
  val numberOfEpisodes: Int,
  val numberOfSeasons: Int,
  val createdAt: LocalDate,
  val updatedAt: LocalDate,
  val lastUpdate: LocalDate?,
  val seasonLastUpdate: Int,
  val nextAirDate: LocalDate?,
  val nextAirDateIsNewSeason: Boolean?
)