package org.dailyepisode.subscription

interface SubscriptionService {
  fun createSubscription(subscription: Subscription): Subscription
  fun findAll(): List<Subscription>
  fun findById(subscriptionId: Long): Subscription?
}