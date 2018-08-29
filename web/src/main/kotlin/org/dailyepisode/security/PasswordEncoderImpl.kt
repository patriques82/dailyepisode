package org.dailyepisode.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
internal class PasswordEncoderImpl: PasswordEncoder {
  private val encoder = BCryptPasswordEncoder()

  override fun encode(rawPassword: CharSequence?): String =
    encoder.encode(rawPassword)

  override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean =
    encoder.matches(rawPassword, encodedPassword)

}