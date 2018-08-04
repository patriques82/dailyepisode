package org.dailyepisode.subscription

import javax.persistence.*

@Entity
@Table(name = "subscription")
data class SubscriptionEntity(

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  val remoteId: Int,
  val name: String,

  @Column(length = 1000)
  val overview: String,
  val imageUrl: String
)