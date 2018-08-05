package org.dailyepisode.account

interface AccountService {
  fun createAccount(account: Account, password: String): Account
  fun findAll(): List<Account>
}