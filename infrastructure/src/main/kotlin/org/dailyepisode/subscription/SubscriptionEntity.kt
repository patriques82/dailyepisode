package org.dailyepisode.subscription

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class SubscriptionEntity(
  @Id
  @GeneratedValue
  val id: Int,
  val remoteId: Int,
  val name: String,

  @Column(length = 1000)
  val overview: String,
  val thumbnailUrl: String
)