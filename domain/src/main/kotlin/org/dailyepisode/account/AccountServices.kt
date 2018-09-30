package org.dailyepisode.account

import org.dailyepisode.subscription.Subscription

interface AccountStorageService {
  fun createAccount(accountRegistrationRequest: AccountRegistrationRequest)
  fun updateAccount(accountId: Long, accountUpdateRequest: AccountUpdateRequest)
  fun updatePassword(passwordUpdateRequest: PasswordUpdateRequest)
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
  val username: String,
  val notificationIntervalInDays: Int
)

data class PasswordUpdateRequest(
  val id: Long,
  val newPassword: String
)

interface AccountResolverService {
  fun resolve(): Account
}