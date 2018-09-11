package org.dailyepisode.controller.admin

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountRepository
import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.dto.AccountRegistrationRequestDto
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class AdminAccountControllerTest: AbstractControllerIntegrationTest() {

  @MockBean
  lateinit var accountRepository: AccountRepository

  @Test
  @WithMockUser(roles = arrayOf("USER"))
  fun `user role access should return 403 Forbidden`() {
    mockMvc.perform(get("/admin/user")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden)
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `register with no body should return 400 Bad Request`() {
    mockMvc.perform(post("/admin/user")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(null)))
      .andExpect(status().isBadRequest)
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `register with invalid account should return 400 Bad Request`() {
    val accountRegistrationRequestDto = AccountRegistrationRequestDto("INVALID_USERNAME", "INVALID_EMAIL", "INVALID_PASSWORD", 1)
    mockMvc.perform(post("/admin/user")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(accountRegistrationRequestDto)))
      .andExpect(status().isBadRequest)
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `register with valid account should return 201 Created`() {
    val accountRegistrationRequestDto = AccountRegistrationRequestDto("user", "user@email.com", "P@ssw0rd", 1)
    mockMvc.perform(post("/admin/user")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(accountRegistrationRequestDto)))
      .andExpect(status().isBadRequest)
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `get all accounts should return accounts and 200 Ok`() {
    val homer = AccountEntity(1L, "Homer", "homer.simpson@yahoo.com", "password", 1, listOf(), emptyList())
    val marge = AccountEntity(2L, "Marge", "marge.simpson@yahoo.com", "password", 2, listOf(), emptyList())
    given(accountRepository.findAll()).willReturn(listOf(homer, marge))

    val expectedJson = """[
      {"id":1,"username":"Homer","email":"homer.simpson@yahoo.com","notificationIntervalInDays":1},
      {"id":2,"username":"Marge","email":"marge.simpson@yahoo.com","notificationIntervalInDays":2}
      ]""".trimIndent()

    mockMvc.perform(get("/admin/user")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `get account with existing account id should return account and 200 Ok`() {
    val homer = AccountEntity(1L, "Homer", "homer.simpson@yahoo.com", "password", 1, listOf(), emptyList())
    given(accountRepository.findById(1L)).willReturn(Optional.of(homer))

    val expectedJson = """
      {"id":1,"username":"Homer","email":"homer.simpson@yahoo.com","notificationIntervalInDays":1},
      """.trimIndent()

    mockMvc.perform(get("/admin/user/1")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `get account with non-existing account id should return 204 No Content`() {
    given(accountRepository.findById(1L)).willReturn(Optional.empty())

    mockMvc.perform(get("/admin/user/1")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent)
  }

}