package org.dailyepisode.controller.user

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountRepository
import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.dto.SubscriptionPreferencesRequestDto
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AccountControllerTest: AbstractControllerIntegrationTest() {

  @MockBean
  private lateinit var accountRepository: AccountRepository

  private val account = AccountEntity(1, "tester", "email", "pass", 1)

  @Test
  @WithMockUser
  fun `unknown user access should return 403 Forbidden`() {
    mockMvc.perform(get("/api/user/me")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden)
  }

  @Test
  @WithMockUser(username = "tester", roles = arrayOf("USER"))
  fun `get current account with valid user account should return user details and 200 Ok`() {
    given(accountRepository.findByUsername("tester")).willReturn(account)

    val expectedJson = """
      {"id":1,"username":"tester","email":"email","notificationIntervalInDays":1}
    """.trimIndent()

    mockMvc.perform(get("/api/user/me")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  @WithMockUser(username = "tester", roles = arrayOf("USER"))
  fun `update preferences with invalid data should return 400 Bad Request`() {
    given(accountRepository.findByUsername("tester")).willReturn(account)

    val invalidPreferencesRequestDto = SubscriptionPreferencesRequestDto(notificationIntervalInDays = -1)

    mockMvc.perform(put("/api/user/preferences")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(invalidPreferencesRequestDto)))
      .andExpect(status().isBadRequest)
  }

  @Test
  @WithMockUser(username = "tester", roles = arrayOf("USER"))
  fun `update preferences with valid data should return 201 Created`() {
    given(accountRepository.findByUsername("tester")).willReturn(account)

    val preferencesRequestDto = SubscriptionPreferencesRequestDto(notificationIntervalInDays = 3)

    mockMvc.perform(put("/api/user/preferences")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(preferencesRequestDto)))
      .andExpect(status().isCreated)
  }

}