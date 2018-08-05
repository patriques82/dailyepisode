package org.dailyepisode.account

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
internal class AccountServiceImpl(val accountRepository: AccountRepository,
                                  val passwordEncoder: PasswordEncoder): AccountService {

  override fun createAccount(account: Account, password: String): Account {
    val storedAccount =
      accountRepository.findByEmail(account.email) ?:
      accountRepository.save(account.toEntity(password))
    return storedAccount.toAccount()
  }

  override fun findAll(): List<Account> {
    return accountRepository.findAll().map { it.toAccount() }
  }

  private fun AccountEntity.toAccount(): Account =
    Account(id, username, email)

  private fun Account.toEntity(password: String): AccountEntity =
    AccountEntity(id, username, email, passwordEncoder.encode(password), emptyList())
}


