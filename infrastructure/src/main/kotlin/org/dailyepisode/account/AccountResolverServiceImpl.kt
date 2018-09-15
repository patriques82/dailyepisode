package org.dailyepisode.account

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AccountResolverServiceImpl(private val accountStorageService: AccountStorageService): AccountResolverService {
  override fun resolve(): Account {
    val userName = SecurityContextHolder.getContext().authentication.name
    return accountStorageService.findByUserName(userName) ?: throw ForbiddenAccessException("Access denied")
  }
}