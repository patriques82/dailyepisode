package org.dailyepisode.account

import org.dailyepisode.subscription.Subscription

interface AccountResolver {
  fun resolve(): FulfilledAccount
}

data class FulfilledAccount(
  val id: Long,
  val username: String,
  val email: String,
  val notificationIntervalInDays: Int,
  val roles: List<String>,
  val subscriptions: List<Subscription>
)