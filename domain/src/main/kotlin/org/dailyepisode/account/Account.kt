package org.dailyepisode.account

import org.dailyepisode.subscription.Subscription

data class Account(
  val id: Long?,
  val username: String,
  val email: String,
  val password: String,
  val subscriptions: List<Subscription>
)
