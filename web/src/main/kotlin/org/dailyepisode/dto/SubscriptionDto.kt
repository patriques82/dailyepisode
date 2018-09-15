package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.CreateSubscriptionsRequest

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SubscriptionRequestDto(
  val accountId: Long,
  val remoteIds: List<Int>
)

fun SubscriptionRequestDto.toSubscriptionRequest(): CreateSubscriptionsRequest =
  CreateSubscriptionsRequest(accountId, remoteIds)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SubscriptionDto(
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

fun Subscription.toDto(): SubscriptionDto =
  SubscriptionDto(id, remoteId, name, overview, imageUrl, voteCount, voteAverage, firstAirDate, lastAirDate,
    genres, homepage, numberOfEpisodes, numberOfSeasons)