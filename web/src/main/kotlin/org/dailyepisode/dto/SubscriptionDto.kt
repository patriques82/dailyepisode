package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.SubscriptionCreateRequest

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SubscriptionRequestDto(
  val accountId: Long,
  val remoteId: Int
)

fun SubscriptionRequestDto.toSubscriptionRequest(): SubscriptionCreateRequest =
  SubscriptionCreateRequest(accountId, remoteId)

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
  SubscriptionDto(id, remoteId, name, overview, imageUrl, voteCount, voteAverage,
    firstAirDate?.toString(), lastAirDate?.toString(), genres, homepage, numberOfEpisodes, numberOfSeasons)