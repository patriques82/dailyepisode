package org.dailyepisode.subscription

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository: CrudRepository<SubscriptionDto, Long>
