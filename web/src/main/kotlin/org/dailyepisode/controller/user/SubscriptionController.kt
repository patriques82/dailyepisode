package org.dailyepisode.controller.user

import org.dailyepisode.dto.SubscriptionDto
import org.dailyepisode.dto.toDto
import org.dailyepisode.dto.toSubscription
import org.dailyepisode.subscription.SubscriptionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@Secured("ROLE_USER")
@RequestMapping("/api/subscription")
class SubscriptionController(val subscriptionService: SubscriptionService) {

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
  fun getSubscription(@PathVariable subscriptionId: Long): ResponseEntity<SubscriptionDto> {
    val subscription = subscriptionService.findById(subscriptionId)
    if (subscription != null) {
      return ResponseEntity.ok(subscription.toDto())
    } else {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }
}

