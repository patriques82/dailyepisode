package org.dailyepisode.controller

import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.SubscriptionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscription")
class SubscriptionController(val subscriptionService: SubscriptionService) {

  @PostMapping
  fun createSubscription(@RequestBody data: Subscription?): ResponseEntity<Subscription> {
    if (data != null) {
      return ResponseEntity.ok(subscriptionService.createSubscription(data))
    } else {
      return ResponseEntity.unprocessableEntity().build()
    }
  }

  @GetMapping
  fun getAllSubscriptions(): ResponseEntity<List<Subscription>> =
    ResponseEntity.ok(subscriptionService.getAll())

}

