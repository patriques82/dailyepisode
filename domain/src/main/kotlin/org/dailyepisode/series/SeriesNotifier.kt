package org.dailyepisode.series

interface SeriesNotifier<in REQ: NotificationRequest> {
  fun send(notificationRequest: REQ)
}

interface NotificationRequest

