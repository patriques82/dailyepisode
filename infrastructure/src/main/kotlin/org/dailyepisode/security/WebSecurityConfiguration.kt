package org.dailyepisode.security

import org.dailyepisode.account.AccountRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
internal class WebSecurityConfiguration(val accountRepository: AccountRepository,
                                        @Value("\${cors.allowed.origin}") val allowedOrigin: String) {

  @Bean
  fun corsConfigurer(): WebMvcConfigurer {
    return object : WebMvcConfigurer {
      override fun addCorsMappings(registry: CorsRegistry) {
        registry
          .addMapping("/api/*")
          .allowedOrigins(allowedOrigin)
      }
    }
  }

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