package org.dailyepisode.account

import org.dailyepisode.exception.NoAccountFoundException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
internal class AccountResolverImpl(private val accountService: AccountService): AccountResolver {
  override fun resolve(): FulfilledAccount {
    val userName = SecurityContextHolder.getContext().authentication.name
    val account = accountService.findByUserName(userName) ?: throw NoAccountFoundException("No account found")
    return account.toFulfilledAccount()
  }
}