package org.dailyepisode.controller

import org.dailyepisode.dto.SubscriptionDto
import org.dailyepisode.subscription.SubscriptionService
import org.dailyepisode.dto.toSubscription
import org.dailyepisode.dto.toDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscription")
class SubscriptionController(val subscriptionService: SubscriptionService) {

  @PostMapping
  fun createSubscription(@RequestBody subscription: SubscriptionDto?): ResponseEntity<SubscriptionDto> {
    if (subscription != null) {
      val subscriptionResponse = subscriptionService.createSubscription(subscription.toSubscription())
      return ResponseEntity.ok(subscriptionResponse.toDto())
    } else {
      return ResponseEntity.unprocessableEntity().build()
    }
  }

  @GetMapping
  fun getAllSubscriptions(): ResponseEntity<List<SubscriptionDto>> =
    ResponseEntity.ok(subscriptionService.getAll().map { it.toDto() })

}

