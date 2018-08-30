package org.dailyepisode.account

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
internal class AccountResolverImpl(private val accountService: AccountService): AccountResolver {
  override fun resolve(): Account? {
    val userName = SecurityContextHolder.getContext().authentication.name
    return accountService.findByUserName(userName)
  }
}