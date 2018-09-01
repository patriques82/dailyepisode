package org.dailyepisode.account

import org.dailyepisode.subscription.Subscription

data class Account(
  val id: Long?,
  val username: String,
  val email: String,
  val password: String?,
  val notificationIntervalInDays: Int,
  val roles: List<String>,
  val subscriptions: List<Subscription>
)

data class FulfilledAccount(
  val id: Long,
  val username: String,
  val email: String,
  val notificationIntervalInDays: Int,
  val roles: List<String>,
  val subscriptions: List<Subscription>
)