package org.dailyepisode.account

import org.dailyepisode.subscription.Subscription
import java.time.LocalDateTime
import java.util.*

interface AccountStorageService {
  fun createAccount(accountRegistrationRequest: AccountRegistrationRequest): Account
  fun updateAccount(accountId: Long, accountUpdateRequest: AccountUpdateRequest)
  fun updatePassword(accountId: Long, passwordUpdateRequest: PasswordUpdateRequest)
  fun updateNotifiedAt(accountId: Long, date: Date)
  fun findAll(): List<Account>
  fun findById(accountId: Long): Account?
  fun findByUserName(userName: String): Account?
  fun delete(accountId: Long)
}

data class AccountRegistrationRequest(
  val username: String,
  val email: String,
  val password: String,
  val notificationIntervalInDays: Int,
  val isAdmin: Boolean
)

data class Account(
  val id: Long,
  val username: String,
  val email: String,
  val password: String?,
  val notificationIntervalInDays: Int,
  val isAdmin: Boolean,
  val subscriptions: List<Subscription>,
  val createdAt: LocalDateTime,
  val notifiedAt: LocalDateTime
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