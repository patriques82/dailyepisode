package org.dailyepisode.controller.admin

import org.dailyepisode.account.AccountService
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.toDto
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/admin/user")
class AccountController(val accountService: AccountService) {

  @GetMapping
  fun findAllAccounts(): ResponseEntity<List<AccountDto>> {
    val authentication = SecurityContextHolder.getContext().authentication
    authentication.credentials
    val accounts = accountService.findAll().map { it.toDto()}
    return ResponseEntity.ok(accounts)
  }

}