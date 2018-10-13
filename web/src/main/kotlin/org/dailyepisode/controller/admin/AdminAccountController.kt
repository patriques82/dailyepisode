package org.dailyepisode.controller.admin

import org.dailyepisode.account.AccountRegistrationRequest
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.dto.AccountRegistrationRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/user")
@CrossOrigin(origins = arrayOf("http://localhost:3000"))
class AdminAccountController(private val accountStorageService: AccountStorageService) {

  @PostMapping
  fun register(@RequestBody registrationRequestDto: AccountRegistrationRequestDto?): ResponseEntity<Unit> {
    return if (registrationRequestDto != null) {
      accountStorageService.createAccount(registrationRequestDto.toAccountRegistrationRequest())
      ResponseEntity(HttpStatus.CREATED)
    } else {
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun AccountRegistrationRequestDto.toAccountRegistrationRequest(): AccountRegistrationRequest =
    AccountRegistrationRequest(username, email, password)

}
