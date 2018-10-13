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

}