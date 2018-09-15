package org.dailyepisode.controller.user

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountResolver
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.SubscriptionPreferencesRequestDto
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

  @PutMapping("/preferences")
  fun updatePreferences(@RequestBody preferencesRequestDto: SubscriptionPreferencesRequestDto): ResponseEntity<Unit> {
    val account = accountResolver.resolve()
    return if (isValidSubscriptionPreferences(preferencesRequestDto)) {
      accountStorageService.updateNotificationIntervaInlDays(account.id, preferencesRequestDto.notificationIntervalInDays)
      ResponseEntity(HttpStatus.CREATED)
    } else {
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun isValidSubscriptionPreferences(preferencesRequestDto: SubscriptionPreferencesRequestDto) =
    preferencesRequestDto.notificationIntervalInDays > 0

}

