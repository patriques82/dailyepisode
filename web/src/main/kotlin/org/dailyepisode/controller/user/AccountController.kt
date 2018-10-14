package org.dailyepisode.controller.user

import org.dailyepisode.account.*
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.AccountUpdateRequestDto
import org.dailyepisode.dto.PasswordChangeRequestDto
import org.dailyepisode.dto.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class AccountController(private val accountStorageService: AccountStorageService,
                        private val accountResolverService: AccountResolverService) {
  @GetMapping
  fun getAllAccounts(): ResponseEntity<List<AccountDto>> {
    val accounts = accountStorageService.findAll().map { it.toDto()}
    return ResponseEntity(accounts, HttpStatus.OK)
  }

  @GetMapping("/{accountId}")
  fun getAccount(@PathVariable accountId: Long): ResponseEntity<AccountDto> {
    val account: Account? = accountStorageService.findById(accountId)
    if (account == null) {
      return ResponseEntity(HttpStatus.NO_CONTENT)
    }
    return ResponseEntity(account.toDto(), HttpStatus.OK)
  }

  @GetMapping("/me")
  fun getCurrentAccount(): ResponseEntity<AccountDto> {
    val account = accountResolverService.resolve()
    return ResponseEntity(account.toDto(), HttpStatus.OK)
  }

  @PutMapping("/update")
  fun updatePreferences(@RequestBody accountUpdateRequestDto: AccountUpdateRequestDto?): ResponseEntity<Unit> {
    val account = accountResolverService.resolve()
    if (accountUpdateRequestDto != null) {
      accountStorageService.updateAccount(account.id, accountUpdateRequestDto.toUpdateAccountRequest())
      return ResponseEntity(HttpStatus.CREATED)
    } else {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun AccountUpdateRequestDto.toUpdateAccountRequest() =
    AccountUpdateRequest(accountId, username, notificationIntervalInDays)

  @PutMapping("/change-password")
  fun changePassword(@RequestBody changePasswordChangeRequestDto: PasswordChangeRequestDto?): ResponseEntity<Unit> {
    val account = accountResolverService.resolve()
    if (changePasswordChangeRequestDto != null) {
      accountStorageService.updatePassword(account.id, changePasswordChangeRequestDto.toPasswordChangeRequest())
      return ResponseEntity(HttpStatus.CREATED)
    } else {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun PasswordChangeRequestDto.toPasswordChangeRequest(): PasswordUpdateRequest =
    PasswordUpdateRequest(accountId, newPassword)

  @DeleteMapping
  fun deleteAccount(): ResponseEntity<Unit> {
    val account = accountResolverService.resolve()
    accountStorageService.delete(account.id)
    return ResponseEntity(HttpStatus.ACCEPTED)
  }

}

