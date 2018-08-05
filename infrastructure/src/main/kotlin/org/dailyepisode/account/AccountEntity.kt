package org.dailyepisode.account

import org.dailyepisode.subscription.SubscriptionEntity
import javax.persistence.*

@Entity
@Table(name = "account")
class AccountEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,
  var username: String,
  var email: String,
  var password: String,

  @ManyToMany(cascade = arrayOf(CascadeType.ALL))
  @JoinTable(
    name = "account_subscription",
    joinColumns = arrayOf(JoinColumn(name = "account_id", referencedColumnName = "id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "subscription_id", referencedColumnName = "id")))
  var subscriptions: List<SubscriptionEntity>
)

