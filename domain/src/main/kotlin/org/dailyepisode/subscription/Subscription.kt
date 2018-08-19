package org.dailyepisode.subscription

data class Subscription(
  val id: Long?,
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String
)

