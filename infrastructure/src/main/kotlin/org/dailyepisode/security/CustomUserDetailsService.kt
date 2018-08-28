package org.dailyepisode.security

import org.dailyepisode.account.AccountRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
internal class CustomUserDetailsService(private val accountRepository: AccountRepository): UserDetailsService {

  override fun loadUserByUsername(username: String?): UserDetails {
    val accountEntity = username?.let { accountRepository.findByUsername(it) }
    return UserDetailsFactory.create(accountEntity)
  }

}