package org.dailyepisode.account

import org.dailyepisode.subscription.Subscription

interface AccountStorageService {
  fun createAccount(accountRegistrationRequest: AccountRegistrationRequest)
  fun updateAccount(accountUpdateRequest: AccountUpdateRequest)
  fun findAll(): List<Account>
  fun findById(accountId: Long): Account?
  fun findByUserName(userName: String): Account?
}

data class AccountRegistrationRequest(
  val username: String?,
  val email: String?,
  val password: String?
)

data class Account(
  val id: Long,
  val username: String,
  val email: String,
  val password: String?,
  val notificationIntervalInDays: Int,
  val roles: List<String>,
  val subscriptions: List<Subscription>
)

data class AccountUpdateRequest(
  val id: Long,
  val notificationIntervalInDays: Int
)