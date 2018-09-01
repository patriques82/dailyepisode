package org.dailyepisode.account

import org.dailyepisode.exception.EmailAlreadyInUseException
import org.dailyepisode.exception.InvalidAccountException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
internal class AccountServiceImpl(private val accountRepository: AccountRepository,
                                  private val passwordEncoder: PasswordEncoder): AccountService {

  override fun createAccount(account: Account) {
    AccountValidator.validate(account)
    val storedAccount = accountRepository.findByEmail(account.email)
    if (storedAccount != null) {
      throw EmailAlreadyInUseException("Account with email: ${account.email} is already in use")
    }
    accountRepository.save(account.toEntity())
  }

  private fun Account.toEntity(): AccountEntity =
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


