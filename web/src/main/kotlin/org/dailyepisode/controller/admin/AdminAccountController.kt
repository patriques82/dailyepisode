package org.dailyepisode.controller.admin

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountService
import org.dailyepisode.account.AccountValidator
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.AccountRegistrationDto
import org.dailyepisode.dto.toAccount
import org.dailyepisode.dto.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/user")
class AdminAccountController(private val accountService: AccountService) {

  @PostMapping
  fun register(@RequestBody registrationDto: AccountRegistrationDto?): ResponseEntity<Unit> {
    return if (registrationDto != null) {
      accountService.createAccount(registrationDto.toAccount())
      ResponseEntity(HttpStatus.CREATED)
    } else {
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  @GetMapping
  fun findAllAccounts(): ResponseEntity<List<AccountDto>> {
    val accounts = accountService.findAll().map { it.toDto()}
    return ResponseEntity(accounts, HttpStatus.OK)
  }

  @GetMapping("/{accountId}")
  fun findAccount(@PathVariable accountId: Long): ResponseEntity<AccountDto> {
    val account: Account? = accountService.findById(accountId)
    if (account == null) {
      return ResponseEntity(HttpStatus.NO_CONTENT)
    }
    return ResponseEntity(account.toDto(), HttpStatus.OK)
  }

}