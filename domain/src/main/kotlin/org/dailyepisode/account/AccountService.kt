package org.dailyepisode.account

interface AccountService {
  fun createAccount(account: Account, password: String): Account
  fun findAll(): List<Account>
  fun findById(accountId: Long): Account?
  fun findByUserName(userName: String): Account?
}