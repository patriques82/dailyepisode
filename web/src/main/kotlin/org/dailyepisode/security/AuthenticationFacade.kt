package org.dailyepisode.security

import org.dailyepisode.account.AccountService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

interface UserNameResolver {
  fun get(): String
}

@Service
internal class UserNameResolverImpl(val accountService: AccountService): UserNameResolver {
  override fun get(): String = SecurityContextHolder.getContext().authentication.name
}