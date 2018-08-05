package org.dailyepisode.security

import org.dailyepisode.account.AccountRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
internal class WebSecurityConfiguration(val accountRepository: AccountRepository) {
  @Bean
  fun passwordEncoder(): PasswordEncoder =
    BCryptPasswordEncoder()

  @Bean
  fun userDetailsService(): UserDetailsService = UserDetailsService {
    val account = accountRepository.findByUsername(it)
    User.builder()
        .username(account?.username)
        .password(account?.password)
        .authorities("USER", "write")
        .build()
      ?: throw UsernameNotFoundException("Could not find user with username: '$it'")
  }
}