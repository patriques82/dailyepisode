package org.dailyepisode.account

import org.junit.Test

class AccountValidatorTest {
  private val validUsername = "valid_username"
  private val INVALID_USERNAME = "Invalid_username"
  private val validEmail = "valid@email.com"
  private val INVALID_EMAIL = "invalid_email.com"
  private val validPassword = "valid_P4SSWORD"
  private val INVALID_PASSWORD = "invalid_Password"

  @Test(expected = InvalidUserNameException::class)
  fun `validate registration request with invalid username should throw InvalidAccountException`() {
    val invalidRequest = AccountRegistrationRequest(INVALID_USERNAME, validEmail, validPassword)
    AccountValidator.validate(invalidRequest)
  }

  @Test(expected = InvalidEmailException::class)
  fun `validate registration request with invalid email should throw InvalidAccountException`() {
    val invalidRequest = AccountRegistrationRequest(validUsername, INVALID_EMAIL, validPassword)
    AccountValidator.validate(invalidRequest)
  }

  @Test(expected = InvalidPasswordException::class)
  fun `validate registration request with invalid password should throw InvalidAccountException`() {
    val invalidRequest = AccountRegistrationRequest(validUsername, validEmail, INVALID_PASSWORD)
    AccountValidator.validate(invalidRequest)
  }

}