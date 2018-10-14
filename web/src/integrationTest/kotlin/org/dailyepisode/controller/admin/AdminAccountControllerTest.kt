package org.dailyepisode.controller.admin

import org.assertj.core.api.Assertions.assertThat
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.dto.AccountRegistrationRequestDto
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AdminAccountControllerTest : AbstractControllerIntegrationTest() {

  @Autowired
  private lateinit var accountStorageService: AccountStorageService

  @Test
  fun `user role access should return 403 Forbidden`() {
    mockMvc.perform(get("/admin/user")
      .with(csrf())
      .with(httpBasic("kristoffer", "reffotsirk"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden)
  }

  @Test
  fun `register with no body should return 400 Bad Request`() {
    mockMvc.perform(post("/admin/user")
      .with(csrf())
      .with(httpBasic("patrik", "kirtap"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(null)))
      .andExpect(status().isBadRequest)
  }

  @Test
  fun `register with invalid account should return 400 Bad Request`() {
    val accountRegistrationRequestDto = AccountRegistrationRequestDto("INVALID_USERNAME", "INVALID_EMAIL", "INVALID_PASSWORD", 1, false)

    mockMvc.perform(post("/admin/user")
      .with(csrf())
      .with(httpBasic("patrik", "kirtap"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(accountRegistrationRequestDto)))
      .andExpect(status().isBadRequest)

    val account = accountStorageService.findByUserName("INVALID_USERNAME")
    assertThat(account).isNull()
  }

  @Test
  fun `register with valid account should return accound and 201 Created`() {
    val accountRegistrationRequestDto = AccountRegistrationRequestDto("user", "user@email.com", "Passw0rd", 1, false)

    val expectedJson = """
        {"username": "user",
         "email": "user@email.com",
         "notificationIntervalInDays": 1,
         "nrOfSubscriptions": 0,
         "admin": false
         }
    """.trimIndent()

    mockMvc.perform(post("/admin/user")
      .with(csrf())
      .with(httpBasic("patrik", "kirtap"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(accountRegistrationRequestDto)))
      .andExpect(status().isCreated)
      .andExpect(content().json(expectedJson, false))

    val account = accountStorageService.findByUserName("user")
    assertThat(account).isNotNull()
  }

  @Test
  fun `delete invalid account should return 409 Conflict`() {
    mockMvc.perform(delete("/admin/user/666")
      .with(csrf())
      .with(httpBasic("patrik", "kirtap"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isConflict)
  }

  @Test
  fun `delete valid account should return 202 Accepted`() {
    val accountBefore = accountStorageService.findByUserName("kristoffer")!!

    mockMvc.perform(delete("/admin/user/${accountBefore.id}")
      .with(csrf())
      .with(httpBasic("patrik", "kirtap"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isAccepted)

    val accountAfter = accountStorageService.findByUserName("kristoffer")
    assertThat(accountAfter).isNull()
  }

}