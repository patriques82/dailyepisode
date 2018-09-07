package org.dailyepisode.controller.user

import org.dailyepisode.account.AccountResolver
import org.dailyepisode.dto.SubscriptionDto
import org.dailyepisode.dto.SubscriptionRequestDto
import org.dailyepisode.dto.toDto
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
  fun createSubscription(@RequestBody subscriptionRequestDto: SubscriptionRequestDto?): ResponseEntity<Unit> {
    return if (subscriptionRequestDto != null) {
      val account = accountResolver.resolve()
      subscriptionService.createSubscription(subscriptionRequestDto.remoteIds, account.id)
      ResponseEntity(HttpStatus.CREATED)
    } else {
      ResponseEntity(HttpStatus.NO_CONTENT)
    }
  }

  @GetMapping
  fun getSubscriptions(servletRequest: HttpServletRequest): ResponseEntity<List<SubscriptionDto>> {
    val account = accountResolver.resolve()
    val subscriptions = account.subscriptions.map { it.toDto() }
    return ResponseEntity(subscriptions, HttpStatus.OK)
  }

  @DeleteMapping("/{subscriptionId}")
  fun removeSubscription(@PathVariable subscriptionId: Long): ResponseEntity<Unit> {
    val account = accountResolver.resolve()
    subscriptionService.deleteSubscription(subscriptionId, account.id)
    return ResponseEntity(HttpStatus.OK)
  }

}

