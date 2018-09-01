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
  var notificationIntervalInDays: Int = 0,

  @ManyToMany(cascade = arrayOf(CascadeType.ALL))
  @JoinTable(
    name = "account_role",
    joinColumns = arrayOf(JoinColumn(name = "account_id", referencedColumnName = "id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "role_id", referencedColumnName = "id")))
  var roles: List<RoleEntity> = emptyList(),

  @ManyToMany(cascade = arrayOf(CascadeType.ALL))
  @JoinTable(
    name = "account_subscription",
    joinColumns = arrayOf(JoinColumn(name = "account_id", referencedColumnName = "id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "subscription_id", referencedColumnName = "id")))
  var subscriptions: List<SubscriptionEntity> = emptyList()
) {

  fun toAccount(): Account =
    Account(id, username, email, password, roles.map { it.roleName }, subscriptions.map { it.toSubscription() })

  fun subscribesTo(subscriptionId: Long): Boolean =
    subscriptions.any { it.id == subscriptionId }

}
