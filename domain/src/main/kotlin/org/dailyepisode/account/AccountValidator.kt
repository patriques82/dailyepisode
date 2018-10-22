package org.dailyepisode.account

import org.apache.commons.validator.routines.EmailValidator

object AccountValidator {
  val VALID_PASSWORD_PATTERN = Regex("^[A-Za-z0-9]{6,}\$")
  val VALID_USERNAME_PATTERN = Regex("^[a-z0-9_-]{3,15}\$")
  val EMAIL_VALIDATOR = EmailValidator.getInstance()

  fun validate(accountRegistrationRequest: AccountRegistrationRequest) {
    with(accountRegistrationRequest) {
      validateUsername(username)
      validateEmail(email)
      validatePassword(password)
    }
  }

  fun validate(accountUpdateRequest: AccountUpdateRequest) {
    with(accountUpdateRequest) {
      validateUsername(username)
      validateNotificationInterval(notificationIntervalInDays)
    }
  }

  fun validatePassword(password: String?) {
    if (password == null) {
        throw InvalidPasswordException("No password received")
    } else {
      if (!password.matches(VALID_PASSWORD_PATTERN)) {
        throw InvalidPasswordException("""Invalid password
        - At least 6 chars
        - Contains any lower, upper case character or digit only
      """.trimIndent())
      }
    }
  }

  private fun validateEmail(email: String?) {
    if (!EMAIL_VALIDATOR.isValid(email)) {
      throw InvalidEmailException("Invalid email address")
    }
  }

  private fun validateUsername(username: String?) {
    if (username == null) {
      throw InvalidUserNameException("No user name received")
    } else {
      if (!username.matches(VALID_USERNAME_PATTERN)) {
        throw InvalidUserNameException("""Invalid username
        - Between 3 to 15 characters
        - Contains any lower case character, digit or special symbol “_-” only
      """.trimIndent())
      }
    }
  }

  fun validateNotificationInterval(notificationIntervalInDays: Int) {
    if (notificationIntervalInDays < 1) {
      throw IllegalNotificationInterval("""Illegal notification interval
        - More than 0 days
      """.trimIndent())
    }
  }

}