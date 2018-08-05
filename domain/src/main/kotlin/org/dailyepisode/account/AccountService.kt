package org.dailyepisode.account

interface AccountService {
  fun createAccount(account: Account): Account
  fun findAll(): List<Account>
}