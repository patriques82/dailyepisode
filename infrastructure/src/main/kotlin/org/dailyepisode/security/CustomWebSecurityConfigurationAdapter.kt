package org.dailyepisode.security

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class CustomWebSecurityConfigurationAdapter(val userDetailsService: UserDetailsService,
                                            val passwordEncoder: PasswordEncoder
): WebSecurityConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    http
      .csrf()
        .disable()
        .headers().frameOptions().sameOrigin() // to enable h2-console
        .and()
      .authorizeRequests()
        //.antMatchers("/api/*").authenticated()
        .antMatchers("/resources/**").permitAll()
        .anyRequest().permitAll()
        .and()
      .formLogin()
        .loginPage("/login")
        .permitAll()
  }

  override fun configure(auth: AuthenticationManagerBuilder) {
    auth
      .userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder)
  }

}