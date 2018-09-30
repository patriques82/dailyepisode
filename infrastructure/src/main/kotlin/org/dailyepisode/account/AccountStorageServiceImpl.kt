package org.dailyepisode.account

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
internal class AccountStorageServiceImpl(private val accountRepository: AccountRepository,
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

  override fun updateAccount(accountId: Long, accountUpdateRequest: AccountUpdateRequest) {
    AccountValidator.validate(accountUpdateRequest)
    val account = accountRepository.findById(accountId).orElse(null)
    if (account == null) {
      throw NoAccountFoundException("No account found for id")
    }
    if (account.id != accountUpdateRequest.id) {
      throw NonMatchingAccoundId("Request account id does not match user account id")
    }
    if (account.username != accountUpdateRequest.username && userNameAlreadyExist(accountUpdateRequest.username)) {
      throw InvalidUserNameException("Username already exists")
    }
    with(accountUpdateRequest) {
      account.notificationIntervalInDays = notificationIntervalInDays
      account.username = username
      accountRepository.save(account)
    }
  }

  private fun userNameAlreadyExist(accountUpdateRequestUsername: String): Boolean =
    accountRepository.findByUsername(accountUpdateRequestUsername) != null

  override fun updatePassword(accountId: Long, passwordUpdateRequest: PasswordUpdateRequest) {
    AccountValidator.validatePassword(passwordUpdateRequest.newPassword)
    val account = accountRepository.findById(accountId).orElse(null)
    if (account == null) {
      throw NoAccountFoundException("No account found for id")
    }
    if (account.id != passwordUpdateRequest.id) {
      throw NonMatchingAccoundId("Request account id does not match user account id")
    }
    with(passwordUpdateRequest) {
      account.password = passwordEncoder.encode(newPassword)
      accountRepository.save(account)
    }
  }
}