package org.dailyepisode.controller

import org.dailyepisode.account.AccountService
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.AccountRegistrationDto
import org.dailyepisode.dto.toAccount
import org.dailyepisode.dto.toDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class AccountController(val accountService: AccountService) {

  @PostMapping
  fun createAccount(@RequestBody registrationDto: AccountRegistrationDto?): ResponseEntity<AccountDto> {
    if (registrationDto != null) {
      val accountResponse = with(registrationDto) {
        accountService.createAccount(toAccount(), password)
      }
      return ResponseEntity.ok(accountResponse.toDto())
    } else {
      return ResponseEntity.noContent().build()
    }
  }

  @GetMapping
  fun findAll(): ResponseEntity<List<AccountDto>> {
    val accounts = accountService.findAll().map { it.toDto()}
    return ResponseEntity.ok(accounts)
  }

}
