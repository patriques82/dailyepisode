package org.dailyepisode.subscription

import org.dailyepisode.account.AccountEntity
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "subscription")
data class SubscriptionEntity(

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  var remoteId: Int,
  var name: String,

  @Column(length = 1000)
  var overview: String,
  var imageUrl: String,

  @ManyToMany(mappedBy = "subscriptions")
  var accounts: List<AccountEntity>

) {
  @Transient
  val subscription: Subscription = Subscription(id, remoteId, name, overview, imageUrl, accounts.map { it.account })
}