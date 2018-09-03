package org.dailyepisode.account

import org.dailyepisode.subscription.Subscription

interface AccountService {
  fun createAccount(account: Account)
  fun updateNotificationIntervaInlDays(accountId: Long, notificationIntervaInlDays: Int)
  fun findAll(): List<Account>
  fun findById(accountId: Long): Account?
  fun findByUserName(userName: String): Account?
}

data class Account(
  val id: Long?,
  val username: String,
  val email: String,
  val password: String?,
  val notificationIntervalInDays: Int,
  val roles: List<String>,
  val subscriptions: List<Subscription>
)