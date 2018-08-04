package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.dailyepisode.subscription.Subscription

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SubscriptionDto(
  val id: Long?,
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String
)

fun SubscriptionDto.toSubscription(): Subscription {
  return Subscription(id, remoteId, name, overview, imageUrl, emptyList())
}

fun Subscription.toDto(): SubscriptionDto {
  return SubscriptionDto(id, remoteId, name, overview, imageUrl)
}