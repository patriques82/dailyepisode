package org.dailyepisode.controller

import org.dailyepisode.account.AccountService
import org.dailyepisode.dto.SubscriptionDto
import org.dailyepisode.subscription.SubscriptionService
import org.dailyepisode.dto.toSubscription
import org.dailyepisode.dto.toDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscription")
class SubscriptionController(val subscriptionService: SubscriptionService,
                             val accountService: AccountService) {

  @PostMapping
  fun createSubscription(@RequestBody subscriptionDto: SubscriptionDto?): ResponseEntity<SubscriptionDto> {
    if (subscriptionDto != null) {
      val subscriptionResponse = subscriptionService.createSubscription(subscriptionDto.toSubscription())
      return ResponseEntity.ok(subscriptionResponse.toDto())
    } else {
      return ResponseEntity.noContent().build()
    }
  }

  @GetMapping
  fun getAllSubscriptions(): ResponseEntity<List<SubscriptionDto>> {
    val subscriptions = subscriptionService.findAll().map { it.toDto() }
    return ResponseEntity.ok(subscriptions)
  }

  @GetMapping("/{subscriptionId}")
  fun getSubscription(@PathVariable subscriptionId: Long): ResponseEntity<SubscriptionDto> {
    val subscription = subscriptionService.findById(subscriptionId)
    if (subscription != null) {
      return ResponseEntity.ok(subscription.toDto())
    } else {
      return ResponseEntity.notFound().build()
    }
  }
}

