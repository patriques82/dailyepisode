package org.dailyepisode.account

import org.apache.commons.validator.routines.EmailValidator

object AccountValidator {
  /*
  - At least 8 chars
  - Contains at least one digit
  - Contains at least one lower alpha char and one upper alpha char
  - Contains at least one char within a set of special chars (@#%$^ etc.)
  - Does not contain space, tab, etc.
  */
  val VALID_PASSWORD_PATTERN = Regex("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")
  /*
   - Between 3 to 15 characters
   - Contains any lower case character, digit or special symbol “_-” only
   */
  val VALID_USERNAME_PATTERN = Regex("^[a-z0-9_-]{3,15}\$")
  val EMAIL_VALIDATOR = EmailValidator.getInstance()

  fun validate(accountRegistrationRequest: AccountRegistrationRequest) {
    with(accountRegistrationRequest) {
      validateUsername(username)
      validateEmail(email)
      validatePassword(password)
    }
  }

  private fun validatePassword(password: String?) {
    if (password == null) {
        throw InvalidPasswordException("No password received")
    } else {
      if (!password.matches(VALID_PASSWORD_PATTERN)) {
        throw InvalidPasswordException("""Invalid password
        - At least 8 chars
        - Contains at least one digit
        - Contains at least one lower alpha char and one upper alpha char
        - Contains at least one char within a set of special chars (@#%${'$'}^ etc.)
        - Does not contain space, tab, etc.
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

}