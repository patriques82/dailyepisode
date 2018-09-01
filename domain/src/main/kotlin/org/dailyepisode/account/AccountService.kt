package org.dailyepisode.account

interface AccountService {
  fun createAccount(account: Account, password: String)
  fun updateNotificationIntervaInlDays(accountId: Long, notificationIntervaInlDays: Int)
  fun findAll(): List<Account>
  fun findById(accountId: Long): Account?
  fun findByUserName(userName: String): Account?
}