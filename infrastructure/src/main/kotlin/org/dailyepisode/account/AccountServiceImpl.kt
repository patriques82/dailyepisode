package org.dailyepisode.account

import org.springframework.stereotype.Service

@Service
internal class AccountServiceImpl(val accountRepository: AccountRepository): AccountService {

  override fun createAccount(account: Account): Account {
    val storedAccount =
      accountRepository.findByEmail(account.email) ?:
      accountRepository.save(account.toEntity())
    return storedAccount.toAccount()
  }

  override fun findAll(): List<Account> {
    return accountRepository.findAll().map { it.toAccount() }
  }
}

private fun AccountEntity.toAccount(): Account =
  Account(id, username, email, password)

private fun Account.toEntity(): AccountEntity =
  AccountEntity(id, username, email, password, emptyList())

