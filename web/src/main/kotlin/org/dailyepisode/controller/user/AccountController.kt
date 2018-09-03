package org.dailyepisode.controller.user

import org.dailyepisode.account.AccountResolver
import org.dailyepisode.account.AccountService
import org.dailyepisode.account.FulfilledAccount
import org.dailyepisode.dto.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/user")
class AccountController(private val accountService: AccountService,
                        private val accountResolver: AccountResolver) {

  @GetMapping
  fun current(servletRequest: HttpServletRequest): ResponseEntity<AccountDto> {
    val account = accountResolver.resolve()
    return ResponseEntity(account.toDto(), HttpStatus.OK)
  }

  @PutMapping("/preferences")
  fun updatePreferences(@RequestBody preferencesDto: SubscriptionPreferencesDto): ResponseEntity<Unit> {
    val account = accountResolver.resolve()
    return if (isValidSubscriptionPreferences(preferencesDto)) {
      accountService.updateNotificationIntervaInlDays(account.id, preferencesDto.notificationIntervalInDays)
      ResponseEntity(HttpStatus.CREATED)
    } else {
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun isValidSubscriptionPreferences(preferencesDto: SubscriptionPreferencesDto) =
    preferencesDto.notificationIntervalInDays > 0

  private fun FulfilledAccount.toDto(): AccountDto =
    AccountDto(id, username, email, notificationIntervalInDays)

}

