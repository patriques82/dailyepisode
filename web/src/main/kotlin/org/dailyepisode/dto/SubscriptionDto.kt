package org.dailyepisode.dto

import org.dailyepisode.subscription.Subscription

data class SubscriptionDto(
  val id: Int?,
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String
)

fun SubscriptionDto.toSubscription(): Subscription {
  return Subscription(id ?: 0, remoteId, name, overview, imageUrl)
}

fun Subscription.toDto(): SubscriptionDto {
  return SubscriptionDto(id, remoteId, name, overview, imageUrl)
}