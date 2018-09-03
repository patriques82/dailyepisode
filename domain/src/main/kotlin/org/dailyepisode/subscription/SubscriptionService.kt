package org.dailyepisode.subscription

interface SubscriptionService {
  fun createSubscription(subscription: Subscription, accountId: Long)
  fun findAll(): List<Subscription>
  fun findById(subscriptionId: Long): Subscription?
  fun deleteSubscription(subscriptionId: Long, accountId: Long)
}

data class Subscription(
  val id: Long?,
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String
)