package org.dailyepisode.controller

import org.dailyepisode.account.AccountService
import org.dailyepisode.account.AccountValidator
import org.dailyepisode.dto.*
import org.dailyepisode.util.UserContextResolver
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class AccountController(val accountService: AccountService,
                        val userContextResolver: UserContextResolver) {

  @PostMapping("/register")
  fun register(@RequestBody registrationDto: AccountRegistrationDto?): ResponseEntity<AccountDto> {
    if (registrationDto != null && isValidRegistrationData(registrationDto)) {
      val accountResponse = with(registrationDto) {
        accountService.createAccount(toAccount(), password!!)
      }
      return ResponseEntity(accountResponse.toDto(), HttpStatus.CREATED)
    } else {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun isValidRegistrationData(registrationDto: AccountRegistrationDto): Boolean {
    val account = registrationDto.toAccount()
    return AccountValidator.isValid(account)
  }

  @PreAuthorize("hasAnyRole('ADMIN')")
  @GetMapping
  fun findAllAccounts(): ResponseEntity<List<AccountDto>> {
    val authentication = SecurityContextHolder.getContext().authentication
    authentication.credentials
    val accounts = accountService.findAll().map { it.toDto()}
    return ResponseEntity.ok(accounts)
  }

}
