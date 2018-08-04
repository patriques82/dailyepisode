package org.dailyepisode.internal

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountRepository
import org.dailyepisode.subscription.SubscriptionRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
internal class DataInitializer(
  val accountRepository: AccountRepository,
  val subscriptionRepository: SubscriptionRepository
): CommandLineRunner {

  override fun run(vararg args: String?) {

    val accounts = mutableListOf<AccountEntity>()
    accounts.add(AccountEntity(null, "Patrik", "patrik@gmail.com", "kirtap", emptyList()))
    accounts.add(AccountEntity(null, "Sixten", "sixten@gmail.com", "netxis", emptyList()))
    accounts.add(AccountEntity(null, "Alexia", "alexia@gmail.com", "aixela", emptyList()))
    accounts.add(AccountEntity(null, "Kristoffer", "kristoffer@gmail.com", "reffotsirk", emptyList()))

    val storedAccounts = accountRepository.saveAll(accounts)
    storedAccounts.forEach { println(it) }

  }

}