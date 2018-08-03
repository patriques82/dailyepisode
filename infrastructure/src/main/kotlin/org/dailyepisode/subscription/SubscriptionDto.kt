package org.dailyepisode.subscription

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class SubscriptionDto(
  @Id
  @GeneratedValue
  val id: Int,
  val name: String
)