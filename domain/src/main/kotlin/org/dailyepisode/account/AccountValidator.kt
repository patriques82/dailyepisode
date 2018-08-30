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
  val VALID_USERNAME_PATTERN = Regex("^[a-zA-Z0-9._-]{3,}\$")

  fun isValid(account: Account): Boolean {
    return with(account) {
      validUsername(username) && validEmail(email) && validPassword(password)
    }
  }

  private fun validPassword(password: String?): Boolean {
    if (password == null) {
      return false
    } else {
      return password.matches(VALID_PASSWORD_PATTERN)
    }
  }

  private fun validEmail(email: String?): Boolean {
    val emailValidator = EmailValidator.getInstance()
    return emailValidator.isValid(email)
  }

  private fun validUsername(username: String?): Boolean {
    if (username == null) {
      return false
    } else {
      return username.matches(VALID_USERNAME_PATTERN)
    }
  }
}