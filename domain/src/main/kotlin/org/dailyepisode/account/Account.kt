package org.dailyepisode.account

import org.dailyepisode.subscription.Subscription

data class Account(
  val id: Long?,
  val username: String,
  val email: String,
  val password: String?,
  val roles: List<String>,
  val subscriptions: List<Subscription>
)

data class FulfilledAccount(
  val id: Long,
  val username: String,
  val email: String,
  val roles: List<String>,
  val subscriptions: List<Subscription>
)

fun Account.toFulfilledAccount(): FulfilledAccount =
  FulfilledAccount(id ?: -1, username, email, roles, subscriptions)