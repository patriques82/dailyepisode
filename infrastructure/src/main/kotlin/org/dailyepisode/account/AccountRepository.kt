package org.dailyepisode.account

import org.dailyepisode.subscription.SubscriptionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Repository
interface AccountRepository: JpaRepository<AccountEntity, Long> {
  fun findByEmail(email: String): AccountEntity?
  fun findByUsername(username: String): AccountEntity?
}

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

  fun toAccount(): Account {
    if (id == null) {
      throw IllegalStateException("Only non transient account entities can be transformed to account")
    }
    return Account(id!!, username, email, password, notificationIntervalInDays, roles.map { it.roleName }, subscriptions.map { it.toSubscription() })
  }

  fun subscribesTo(subscriptionId: Long): Boolean =
    subscriptions.any { it.id == subscriptionId }

}

@Entity
data class RoleEntity(
  @Id
  @GeneratedValue
  val id: Long? = null,

  var roleName: String = "",

  @ManyToMany(mappedBy = "roles")
  var accounts: List<AccountEntity> = emptyList()
)
