package org.dailyepisode.account

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
internal class AccountServiceImpl(private val accountRepository: AccountRepository,
                                  private val passwordEncoder: PasswordEncoder): AccountService {
  override fun findByUserName(userName: String): Account? {
    return accountRepository.findByUsername(userName)?.toAccount()
  }

  override fun findById(accountId: Long): Account? {
    val account = accountRepository.findById(accountId)
    return account.map { it.toAccount() }.orElse(null)
  }

  override fun createAccount(account: Account, password: String): Account {
    val storedAccount =
      accountRepository.findByEmail(account.email) ?:
      accountRepository.save(account.toEntity(password))
    return storedAccount.toAccount()
  }

  override fun findAll(): List<Account> {
    return accountRepository.findAll().map { it.toAccount() }
  }

  private fun Account.toEntity(password: String): AccountEntity =
    AccountEntity(id, username, email, passwordEncoder.encode(password))
}


