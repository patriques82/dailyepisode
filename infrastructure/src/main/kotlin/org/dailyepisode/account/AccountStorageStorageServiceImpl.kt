package org.dailyepisode.account

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
internal class AccountStorageStorageServiceImpl(private val accountRepository: AccountRepository,
                                                private val passwordEncoder: PasswordEncoder): AccountStorageService {

  override fun createAccount(accountRegistrationRequest: AccountRegistrationRequest) {
    AccountValidator.validate(accountRegistrationRequest)
    val storedAccount = accountRepository.findByEmail(accountRegistrationRequest.email!!)
    if (storedAccount != null) {
      throw EmailAlreadyInUseException("Email address is already in use")
    }
    accountRepository.save(accountRegistrationRequest.toEntity())
  }

  private fun AccountRegistrationRequest.toEntity(): AccountEntity =
    AccountEntity(null, username!!, email!!, passwordEncoder.encode(password))

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

  override fun updateAccount(accountUpdateRequest: AccountUpdateRequest) {
    with(accountUpdateRequest) {
      val storedAccount = accountRepository.findById(id).orElse(null)
      if (storedAccount != null) {
        storedAccount.notificationIntervalInDays = notificationIntervalInDays
        accountRepository.save(storedAccount)
      }
    }
  }
}