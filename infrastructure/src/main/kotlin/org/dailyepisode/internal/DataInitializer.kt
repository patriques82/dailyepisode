package org.dailyepisode.internal

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountRepository

import org.dailyepisode.subscription.SubscriptionEntity
import org.dailyepisode.subscription.SubscriptionRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component


// TODO remove me! (create sql file in integration test resource folder)

@Component
@Profile("dev")
internal class DataInitializer(val passwordEncoder: PasswordEncoder,
                               val accountRepository: AccountRepository,
                               val subscriptionRepository: SubscriptionRepository): CommandLineRunner {

  override fun run(vararg args: String?) {
    val gameOfThrones = SubscriptionEntity(null, 1, "game of thrones", "Winter is coming...", "image",
      10, 8.6, "2013-05-15", "2018-05-16", listOf("Fantasy", "Drama"),
      "www.got.com", 72, 8, emptyList())
    val breakingBad = SubscriptionEntity(null, 2, "breaking bad", "Meth dealing tutorial", "image", 29,
      9.1, "2010-01-01", "2014-06-01", listOf("Thriller", "Drama"),
      "www.breakingbad.com", 55, 6, emptyList())
    val lineOfDuty = SubscriptionEntity(null, 3, "line of duty", "Corrupt police investigations", "image",
      6, 7.5, "2009-05-18", "2017-02-29", listOf("Crime", "Drama"),
      "www.lineofduty.com", 48, 5, emptyList())

    val accounts = mutableListOf<AccountEntity>()
    accounts.add(AccountEntity(null,  "patrik", "patrik@gmail.com", passwordEncoder.encode("kirtap"), 1, true, listOf(breakingBad, lineOfDuty)))
    accounts.add(AccountEntity(null, "alexia", "alexia@gmail.com", passwordEncoder.encode("aixela"), 9, false, listOf()))
    accounts.add(AccountEntity(null, "kristoffer", "kristoffer@gmail.com", passwordEncoder.encode("reffotsirk"), 30, false, listOf(lineOfDuty, gameOfThrones)))

    accountRepository.saveAll(accounts)
  }

}