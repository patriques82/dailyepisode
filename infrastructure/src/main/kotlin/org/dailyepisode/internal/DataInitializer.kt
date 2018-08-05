package org.dailyepisode.internal

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountRepository
import org.dailyepisode.subscription.SubscriptionEntity
import org.dailyepisode.subscription.SubscriptionRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
@Profile("dev")
internal class DataInitializer(val passwordEncoder: PasswordEncoder,
                               val accountRepository: AccountRepository,
                               val subscriptionRepository: SubscriptionRepository
): CommandLineRunner {

  override fun run(vararg args: String?) {

    val gameOfThrones = SubscriptionEntity(null, 1, "game of thrones", "winter is coming", "image", emptyList())
    val breakingBad = SubscriptionEntity(null, 2, "breaking bad", "meth", "image", emptyList())
    val lineOfDuty = SubscriptionEntity(null, 3, "line of duty", "corrupt police", "image", emptyList())

    val accounts = mutableListOf<AccountEntity>()
    accounts.add(AccountEntity(null, "Patrik", "patrik@gmail.com", passwordEncoder.encode("kirtap"), listOf(breakingBad, lineOfDuty)))
    accounts.add(AccountEntity(null, "Alexia", "alexia@gmail.com", passwordEncoder.encode("aixela"), listOf(breakingBad, gameOfThrones)))
    accounts.add(AccountEntity(null, "Kristoffer", "kristoffer@gmail.com", passwordEncoder.encode("reffotsirk"), listOf(lineOfDuty, gameOfThrones)))

    val storedAccounts = accountRepository.saveAll(accounts)
    storedAccounts.forEach { println(it) }

  }

}