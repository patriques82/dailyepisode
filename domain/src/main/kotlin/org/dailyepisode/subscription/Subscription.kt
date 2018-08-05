package org.dailyepisode.subscription

import org.dailyepisode.account.Account

data class Subscription(
  val id: Long?,
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String
)

