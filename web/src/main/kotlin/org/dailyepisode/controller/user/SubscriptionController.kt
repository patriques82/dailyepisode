package org.dailyepisode.controller.user

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountResolverService
import org.dailyepisode.dto.*
import org.dailyepisode.subscription.SubscriptionDeleteRequest
import org.dailyepisode.subscription.SubscriptionStorageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscription")
class SubscriptionController(private val subscriptionStorageService: SubscriptionStorageService,
                             private val accountResolverService: AccountResolverService) {

  @PostMapping
  fun createSubscription(@RequestBody subscriptionRequestDto: SubscriptionRequestDto?): ResponseEntity<SubscriptionDto> {
    val account = accountResolverService.resolve()
    if (isValidSubscriptionRequest(account, subscriptionRequestDto)) {
      val subscription = subscriptionStorageService.createSubscriptions(subscriptionRequestDto!!.toSubscriptionRequest())
      if (subscription != null) {
        return ResponseEntity(subscription.toDto(), HttpStatus.CREATED)
      } else {
        return ResponseEntity(HttpStatus.BAD_REQUEST)
      }
    } else {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  private fun isValidSubscriptionRequest(account: Account, subscriptionRequestDto: SubscriptionRequestDto?): Boolean =
    if (subscriptionRequestDto != null) {
      account.id == subscriptionRequestDto.accountId && subscriptionRequestDto.remoteId > 0
    } else {
      false
    }

  @GetMapping
  fun getSubscriptions(): ResponseEntity<List<SubscriptionDto>> {
    val account = accountResolverService.resolve()
    val subscriptions = account.subscriptions.map { it.toDto() }
    return ResponseEntity(subscriptions, HttpStatus.OK)
  }

  @DeleteMapping("/{subscriptionId}")
  fun removeSubscription(@PathVariable subscriptionId: Long): ResponseEntity<Unit> {
    val account = accountResolverService.resolve()
    subscriptionStorageService.deleteSubscription(SubscriptionDeleteRequest(account.id, subscriptionId))
    return ResponseEntity(HttpStatus.OK)
  }

}

