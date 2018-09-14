package org.dailyepisode.controller.user

import org.assertj.core.api.Assertions.assertThat
import org.dailyepisode.account.AccountService
import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.dto.SubscriptionPreferencesRequestDto
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AccountControllerTest: AbstractControllerIntegrationTest() {

  @Autowired
  private lateinit var accountService: AccountService

  @Test
  fun `no user authentication access should return 401 Unauthorized`() {
    mockMvc.perform(get("/api/user/me")
      .with(csrf())
      .with(httpBasic("unknown", "unknown"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized)
  }

  @Test
  fun `get current account with valid user account should return user details and 200 Ok`() {
    val expectedJson = """
      {"username":"alexia","email":"alexia@gmail.com","notificationIntervalInDays":10}
    """.trimIndent()

    mockMvc.perform(get("/api/user/me")
      .with(csrf())
      .with(httpBasic("alexia", "aixela"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  fun `update preferences with invalid data should return 400 Bad Request`() {
    val invalidPreferencesRequestDto = SubscriptionPreferencesRequestDto(notificationIntervalInDays = -1)

    mockMvc.perform(put("/api/user/preferences")
      .with(csrf())
      .with(httpBasic("alexia", "aixela"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(invalidPreferencesRequestDto)))
      .andExpect(status().isBadRequest)
  }

  @Test
  fun `update preferences with valid data should return 201 Created`() {
    val preferencesRequestDto = SubscriptionPreferencesRequestDto(notificationIntervalInDays = 3)

    val kristofferBefore= accountService.findByUserName("kristoffer")!!
    assertThat(kristofferBefore.notificationIntervalInDays).isEqualTo(30)
    mockMvc.perform(put("/api/user/preferences")
      .with(csrf())
      .with(httpBasic("kristoffer", "reffotsirk"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(preferencesRequestDto)))
      .andExpect(status().isCreated)
    val kristofferAfter = accountService.findByUserName("kristoffer")!!
    assertThat(kristofferAfter.notificationIntervalInDays).isEqualTo(3)
  }

}