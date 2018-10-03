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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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
    val accountRegistrationRequestDto = AccountRegistrationRequestDto("INVALID_USERNAME", "INVALID_EMAIL", "INVALID_PASSWORD", 1)

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
  fun `register with valid account should return 201 Created`() {
    val accountRegistrationRequestDto = AccountRegistrationRequestDto("user", "user@email.com", "Passw0rd", 1)

    mockMvc.perform(post("/admin/user")
      .with(csrf())
      .with(httpBasic("patrik", "kirtap"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(accountRegistrationRequestDto)))
      .andExpect(status().isCreated)

    val account = accountStorageService.findByUserName("user")
    assertThat(account).isNotNull()
  }

  @Test
  fun `get all accounts should return accounts and 200 Ok`() {
    val expectedJson = """[
      {"username":"patrik","email":"patrik@gmail.com","notificationIntervalInDays":1},
      {"username":"alexia","email":"alexia@gmail.com","notificationIntervalInDays":9},
      {"username":"kristoffer","email":"kristoffer@gmail.com","notificationIntervalInDays":30}
      ]""".trimIndent()

    mockMvc.perform(get("/admin/user")
      .with(csrf())
      .with(httpBasic("patrik", "kirtap"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  fun `get account with existing account id should return account and 200 Ok`() {
    val kristoffer = accountStorageService.findByUserName("kristoffer")!!

    val expectedJson = """
      {"username":"kristoffer","email":"kristoffer@gmail.com","notificationIntervalInDays":30},
    """.trimIndent()

    mockMvc.perform(get("/admin/user/${kristoffer.id}")
      .with(csrf())
      .with(httpBasic("patrik", "kirtap"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  fun `get account with non-existing account id should return 204 No Content`() {
    mockMvc.perform(get("/admin/user/666")
      .with(csrf())
      .with(httpBasic("patrik", "kirtap"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent)
  }

}