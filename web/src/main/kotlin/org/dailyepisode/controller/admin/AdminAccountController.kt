package org.dailyepisode.controller.admin

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountRegistrationRequest
import org.dailyepisode.account.AccountService
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.AccountRegistrationRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/user")
class AdminAccountController(private val accountService: AccountService) {

  @PostMapping
  fun register(@RequestBody registrationRequestDto: AccountRegistrationRequestDto?): ResponseEntity<Unit> {
    return if (registrationRequestDto != null) {
      accountService.createAccount(registrationRequestDto.toAccountRegistrationRequest())
      ResponseEntity(HttpStatus.CREATED)
    } else {
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun AccountRegistrationRequestDto.toAccountRegistrationRequest(): AccountRegistrationRequest =
    AccountRegistrationRequest(username, email, password)

  @GetMapping
  fun getAllAccounts(): ResponseEntity<List<AccountDto>> {
    val accounts = accountService.findAll().map { it.toDto()}
    return ResponseEntity(accounts, HttpStatus.OK)
  }

  private fun Account.toDto(): AccountDto =
    AccountDto(id, username, email, notificationIntervalInDays)

  @GetMapping("/{accountId}")
  fun getAccount(@PathVariable accountId: Long): ResponseEntity<AccountDto> {
    val account: Account? = accountService.findById(accountId)
    if (account == null) {
      return ResponseEntity(HttpStatus.NO_CONTENT)
    }
    return ResponseEntity(account.toDto(), HttpStatus.OK)
  }

}
