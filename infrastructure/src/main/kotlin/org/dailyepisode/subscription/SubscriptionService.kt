package org.dailyepisode.subscription

interface SubscriptionService {
  fun createSubscription(subscription: Subscription): Subscription
  fun getAll(): List<Subscription>
}