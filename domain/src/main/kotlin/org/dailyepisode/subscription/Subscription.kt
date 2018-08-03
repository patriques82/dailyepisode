package org.dailyepisode.subscription

data class Subscription(
  val id: Int,
  val remoteId: Int,
  val name: String,
  val overview: String,
  val thumbnailUrl: String
)

