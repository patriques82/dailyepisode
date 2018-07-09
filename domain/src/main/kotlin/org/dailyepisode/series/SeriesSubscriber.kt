package org.dailyepisode.series

interface SeriesSubscriber<in REQ: SubscriptionRequest> {
  fun subscribe(subscriptionRequest: REQ)
}

interface SubscriptionRequest

