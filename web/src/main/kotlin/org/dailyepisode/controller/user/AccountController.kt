package org.dailyepisode.controller.user

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountResolverService
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.account.AccountUpdateRequest
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.AccountUpdateRequestDto
import org.dailyepisode.dto.PasswordChangeRequestDto
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
    accountStorageService.updateAccount(account.id, accountUpdateRequestDto!!.toUpdateAccountRequest())
    return ResponseEntity(HttpStatus.CREATED)
  }

  private fun AccountUpdateRequestDto.toUpdateAccountRequest() =
    AccountUpdateRequest(accountId, username, notificationIntervalInDays)

  @PutMapping("/change-password")
  fun changePassword(@RequestBody changePasswordChangeRequestDto: PasswordChangeRequestDto?): ResponseEntity<Unit> {
    //val account = accountResolverService.resolve()

    return ResponseEntity(HttpStatus.NO_CONTENT)
  }


}

