package org.dailyepisode.account

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
internal class AccountServiceImpl(private val accountRepository: AccountRepository,
                                  private val passwordEncoder: PasswordEncoder): AccountService {

  override fun createAccount(account: Account, password: String) {
    val storedAccount = accountRepository.findByEmail(account.email)
    if (storedAccount == null) {
      accountRepository.save(account.toEntity(password))
    }
  }

  private fun Account.toEntity(password: String): AccountEntity =
    AccountEntity(id, username, email, passwordEncoder.encode(password))

  override fun findByUserName(userName: String): Account? {
    return accountRepository.findByUsername(userName)?.toAccount()
  }

  override fun findById(accountId: Long): Account? {
    val account = accountRepository.findById(accountId)
    return account.map { it.toAccount() }.orElse(null)
  }

  override fun findAll(): List<Account> {
    return accountRepository.findAll().map { it.toAccount() }
  }

  override fun updateNotificationIntervaInlDays(accountId: Long, notificationIntervaInlDays: Int) {
    val storedAccount = accountRepository.findById(accountId).orElse(null)
    if (storedAccount != null) {
      storedAccount.notificationIntervalInDays = notificationIntervaInlDays
      accountRepository.save(storedAccount)
    }
  }
}


