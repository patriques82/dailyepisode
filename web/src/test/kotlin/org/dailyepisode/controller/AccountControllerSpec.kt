package org.dailyepisode.controller

import io.mockk.every
import io.mockk.mockk
import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountResolver
import org.dailyepisode.account.AccountService
import org.dailyepisode.controller.user.AccountController
import org.dailyepisode.dto.AccountDto
import org.dailyepisode.dto.AccountRegistrationDto
import org.dailyepisode.dto.toAccount
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.springframework.http.HttpStatus
import java.io.BufferedReader

class AccountControllerSpec: Spek({

  given("a account controller") {

    val validPassword = "p4s?w&Rd1"
    val invalidPassword = "password"

    val accountServiceMock = mockk<AccountService>()
    val validAccountRegistrationDto = AccountRegistrationDto( "tester", "tester@email.com", validPassword)
    val invalidAccountRegistrationDto = AccountRegistrationDto( "t?", "invalid@invalid", invalidPassword)
    every { accountServiceMock.createAccount(validAccountRegistrationDto.toAccount(), any()) } returns validAccountRegistrationDto.toAccount()

    val exampleAccounts = listOf(
      Account(1, "user1", "u1@email.com", validPassword, emptyList(), emptyList()),
      Account(2, "user2", "u2@email.com", validPassword, emptyList(), emptyList())
    )
    every { accountServiceMock.findAll() } returns exampleAccounts

    val dummyAccountResolver = object: AccountResolver {
      override fun resolve(): Account? = null
    }

    val accountController = AccountController(accountServiceMock, dummyAccountResolver)

    on("calling create account with valid registration data") {
      val responseEntity = accountController.register(validAccountRegistrationDto)
      val accountDto = responseEntity.body

      it("should return a status code 201 (created)") {
        assertThat(responseEntity.statusCode, equalTo(HttpStatus.CREATED))
      }

      it("should return the created account") {
        val expectedAccountDto = AccountDto(null, "tester", "tester@email.com")
        assertThat(accountDto, equalTo(expectedAccountDto))
      }
    }

    on("calling create account with no registration data") {
      val responseEntity = accountController.register(null)

      it("should return a status code 400 (bad request)") {
        assertThat(responseEntity.statusCode, equalTo(HttpStatus.BAD_REQUEST))
      }
    }

    on("calling create account with invalid registration data") {
      val responseEntity = accountController.register(invalidAccountRegistrationDto)

      it("should return a status code 400 (bad request)") {
        assertThat(responseEntity.statusCode, equalTo(HttpStatus.BAD_REQUEST))
      }
    }

  }
})