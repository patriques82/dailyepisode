package org.dailyepisode.controller.user

import org.dailyepisode.account.AccountResolver
import org.dailyepisode.dto.SubscriptionDto
import org.dailyepisode.dto.toDto
import org.dailyepisode.dto.toSubscription
import org.dailyepisode.subscription.SubscriptionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/subscription")
class SubscriptionController(private val subscriptionService: SubscriptionService,
                             private val accountResolver: AccountResolver) {

  @PostMapping
  fun createSubscription(@RequestBody subscriptionDto: SubscriptionDto?): ResponseEntity<Unit> {
    if (subscriptionDto != null) {
      val account = accountResolver.resolve()
      subscriptionService.createSubscription(subscriptionDto.toSubscription(), account?.id!!)
      return ResponseEntity(HttpStatus.CREATED)
    } else {
      return ResponseEntity(HttpStatus.NO_CONTENT)
    }
  }

  @GetMapping
  fun getSubscription(servletRequest: HttpServletRequest): ResponseEntity<List<SubscriptionDto>> {
    val account = accountResolver.resolve()
    val subscriptions = account!!.subscriptions.map { it.toDto() }
    return ResponseEntity.ok(subscriptions)
  }

  @DeleteMapping
  fun removeSubscription(@RequestBody subscriptionDto: SubscriptionDto?): ResponseEntity<Unit> {
    return ResponseEntity.ok(Unit)
  }
}

