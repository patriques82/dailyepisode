package org.dailyepisode.controller.user

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountResolver
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
                        private val accountResolver: AccountResolver) {

  @GetMapping("/me")
  fun getCurrentAccount(): ResponseEntity<AccountDto> {
    val account = accountResolver.resolve()
    return ResponseEntity(account.toDto(), HttpStatus.OK)
  }

  private fun Account.toDto(): AccountDto =
    AccountDto(id, username, email, notificationIntervalInDays)

  @PutMapping("/update")
  fun updatePreferences(@RequestBody accountUpdateRequestDto: AccountUpdateRequestDto?): ResponseEntity<Unit> {
    val account = accountResolver.resolve()
    return if (isValidUpdateRequest(account, accountUpdateRequestDto)) {
      accountStorageService.updateAccount(accountUpdateRequestDto!!.toUpdateAccountRequest())
      ResponseEntity(HttpStatus.CREATED)
    } else {
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun isValidUpdateRequest(account: Account, accountUpdateRequestDto: AccountUpdateRequestDto?): Boolean =
    if (accountUpdateRequestDto != null) {
      account.id == accountUpdateRequestDto.accountId && accountUpdateRequestDto.notificationIntervalInDays > 0
    } else {
      false
    }

  private fun AccountUpdateRequestDto.toUpdateAccountRequest() =
    AccountUpdateRequest(accountId, notificationIntervalInDays)
}

