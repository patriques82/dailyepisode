package org.dailyepisode.controller.admin

import org.dailyepisode.account.AccountRegistrationRequest
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.AccountRegistrationRequestDto
import org.dailyepisode.dto.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/user")
@CrossOrigin(origins = arrayOf("http://localhost:3000"))
class AdminAccountController(private val accountStorageService: AccountStorageService) {

  @PostMapping
  fun register(@RequestBody registrationRequestDto: AccountRegistrationRequestDto?): ResponseEntity<AccountDto> {
    return if (registrationRequestDto != null) {
      val account = accountStorageService.createAccount(registrationRequestDto.toAccountRegistrationRequest())
      ResponseEntity(account.toDto(), HttpStatus.CREATED)
    } else {
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun AccountRegistrationRequestDto.toAccountRegistrationRequest(): AccountRegistrationRequest =
    AccountRegistrationRequest(username, email, password, notificationIntervalInDays, isAdmin)

  @DeleteMapping("/{accountId}")
  fun delete(@PathVariable accountId: Long): ResponseEntity<Unit> {
    accountStorageService.delete(accountId)
    return ResponseEntity(HttpStatus.ACCEPTED)
  }

}
