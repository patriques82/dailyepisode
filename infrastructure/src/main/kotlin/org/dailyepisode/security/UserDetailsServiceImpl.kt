package org.dailyepisode.security

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
internal class UserDetailsServiceImpl(private val accountRepository: AccountRepository) : UserDetailsService {

  override fun loadUserByUsername(username: String?): UserDetails {
    val accountEntity = username?.let { accountRepository.findByUsername(it) }
    if (accountEntity == null) {
      throw UsernameNotFoundException("Username: '$username' does not exists")
    }
    return createUserDetails(accountEntity)
  }

  private fun createUserDetails(accountEntity: AccountEntity): UserDetails {
    return object : UserDetails {
      override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val grantedAuthorities = mutableListOf<GrantedAuthority>()
        accountEntity.roles.forEach {
          grantedAuthorities.add(SimpleGrantedAuthority(it.roleName))
        }
        return grantedAuthorities
      }
      override fun getUsername() = accountEntity.username
      override fun getPassword() = accountEntity.password
      override fun isEnabled() = true
      override fun isCredentialsNonExpired() = true
      override fun isAccountNonExpired() = true
      override fun isAccountNonLocked() = true
    }
  }

}