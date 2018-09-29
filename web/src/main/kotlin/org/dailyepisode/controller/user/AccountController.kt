package org.dailyepisode.controller.user

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountResolverService
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.account.AccountUpdateRequest
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.AccountUpdateRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class AccountController(private val accountStorageService: AccountStorageService,
                        private val accountResolverService: AccountResolverService) {

  @GetMapping("/me")
  fun getCurrentAccount(): ResponseEntity<AccountDto> {
    val account = accountResolverService.resolve()
    return ResponseEntity(account.toDto(), HttpStatus.OK)
  }

  private fun Account.toDto(): AccountDto =
    AccountDto(id, username, email, notificationIntervalInDays)

  @PutMapping("/update")
  fun updatePreferences(@RequestBody accountUpdateRequestDto: AccountUpdateRequestDto?): ResponseEntity<Unit> {
    val account = accountResolverService.resolve()
    return if (isValidUpdateRequest(account, accountUpdateRequestDto)) {
      accountStorageService.updateAccount(accountUpdateRequestDto!!.toUpdateAccountRequest())
      ResponseEntity(HttpStatus.CREATED)
    } else {
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun isValidUpdateRequest(account: Account, accountUpdateRequestDto: AccountUpdateRequestDto?): Boolean =
    if (accountUpdateRequestDto != null) {
      isSameAccount(account.id, accountUpdateRequestDto.accountId) &&
        validNotificationInterval(accountUpdateRequestDto.notificationIntervalInDays) &&
          validUsername(account.username, accountUpdateRequestDto.username)
    } else {
      false
    }

  private fun validUsername(username: String, accountUpdateRequestUsername: String): Boolean =
    if (username != accountUpdateRequestUsername) {
      accountStorageService.findByUserName(accountUpdateRequestUsername) == null
    } else {
      true
    }

  private fun validNotificationInterval(notificationIntervalInDays: Int) =
    notificationIntervalInDays > 0

  private fun isSameAccount(accountId: Long, accountUpdateRequestId: Long) =
    accountId == accountUpdateRequestId

  private fun AccountUpdateRequestDto.toUpdateAccountRequest() =
    AccountUpdateRequest(accountId, username, notificationIntervalInDays)
}

