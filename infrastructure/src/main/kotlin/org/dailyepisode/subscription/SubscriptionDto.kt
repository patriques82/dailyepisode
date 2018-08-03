package org.dailyepisode.subscription

data class SubscriptionDto(
  val id: Int?,
  val remoteId: Int,
  val name: String,
  val overview: String,
  val thumbnailUrl: String
)

fun SubscriptionDto.toSubscription(): Subscription {
  return Subscription(id ?: 0, remoteId, name, overview, thumbnailUrl)
}

fun Subscription.toSubscriptionDto(): SubscriptionDto {
  return SubscriptionDto(id, remoteId, name, overview, thumbnailUrl)
}