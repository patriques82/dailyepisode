package org.dailyepisode.controller.user

import org.dailyepisode.account.AccountService
import org.dailyepisode.dto.SubscriptionDto
import org.dailyepisode.dto.toDto
import org.dailyepisode.dto.toSubscription
import org.dailyepisode.security.UserNameResolver
import org.dailyepisode.subscription.SubscriptionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/subscription")
class SubscriptionController(val subscriptionService: SubscriptionService,
                             val accountService: AccountService,
                             val userNameResolver: UserNameResolver) {

  @PostMapping
  fun createSubscription(@RequestBody subscriptionDto: SubscriptionDto?): ResponseEntity<SubscriptionDto> {
    if (subscriptionDto != null) {
      val subscriptionResponse = subscriptionService.createSubscription(subscriptionDto.toSubscription())
      return ResponseEntity.ok(subscriptionResponse.toDto())
    } else {
      return ResponseEntity(HttpStatus.NO_CONTENT)
    }
  }

  @GetMapping
  fun getSubscription(servletRequest: HttpServletRequest): ResponseEntity<List<SubscriptionDto>> {
    val account = accountService.findByUserName(userNameResolver.get())
    return ResponseEntity.ok(account!!.subscriptions.map { it.toDto() })
  }
}

