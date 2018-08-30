package org.dailyepisode.subscription

interface SubscriptionService {
  fun createSubscription(subscription: Subscription, accountId: Long)
  fun findAll(): List<Subscription>
  fun findById(subscriptionId: Long): Subscription?
}