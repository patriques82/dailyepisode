package org.dailyepisode.account

import org.dailyepisode.subscription.SubscriptionEntity
import org.dailyepisode.util.toLocalDate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Repository
interface AccountRepository : JpaRepository<AccountEntity, Long> {
  fun findByEmail(email: String): AccountEntity?
  fun findByUsername(username: String): AccountEntity?
}

@Entity
@Table(name = "account")
class AccountEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,
  @Column(unique = true)
  var username: String,
  var email: String,
  var password: String,
  var notificationIntervalInDays: Int = 0,
  var isAdmin: Boolean = false,

  @ManyToMany(cascade = arrayOf(CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH))
  @JoinTable(
    name = "account_subscription",
    joinColumns = arrayOf(JoinColumn(name = "account_id", referencedColumnName = "id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "subscription_id", referencedColumnName = "id")))
  var subscriptions: List<SubscriptionEntity> = emptyList(),

  @field: CreationTimestamp
  @field: Column(updatable = false)
  val createdAt: Date = Date(),

  @field: UpdateTimestamp
  var notifiedAt: Date = Date()
) {

  fun toAccount(): Account {
    if (id == null) {
      throw IllegalStateException("Only non transient account entities can be transformed to account")
    }
    return Account(id!!, username, email, password, notificationIntervalInDays, isAdmin, subscriptions.map { it.toSubscription() }, createdAt.toLocalDate(), notifiedAt.toLocalDate())
  }

  fun subscribesTo(subscriptionId: Long): Boolean = subscriptions.any { it.id == subscriptionId }

}