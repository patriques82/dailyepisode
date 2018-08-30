package org.dailyepisode.controller.user

import org.dailyepisode.account.AccountService
import org.dailyepisode.account.AccountValidator
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.AccountRegistrationDto
import org.dailyepisode.dto.toAccount
import org.dailyepisode.dto.toDto
import org.dailyepisode.security.UserNameResolver
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/user")
class AccountController(val accountService: AccountService,
                        val userNameResolver: UserNameResolver) {

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

  @GetMapping("/current")
  fun currentUser(servletRequest: HttpServletRequest): ResponseEntity<AccountDto> {
    val account = accountService.findByUserName(userNameResolver.get())
    return ResponseEntity.ok(account!!.toDto())
  }

}