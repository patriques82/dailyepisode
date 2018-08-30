package org.dailyepisode.controller.admin

import org.dailyepisode.dto.SubscriptionDto
import org.dailyepisode.dto.toDto
import org.dailyepisode.subscription.SubscriptionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/subscription")
class AdminSubscriptionController(private val subscriptionService: SubscriptionService) {

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
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }
}