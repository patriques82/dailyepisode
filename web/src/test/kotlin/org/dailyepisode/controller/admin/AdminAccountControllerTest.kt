package org.dailyepisode.controller.admin

import com.fasterxml.jackson.databind.ObjectMapper
import org.dailyepisode.account.AccountService
import org.dailyepisode.dto.AccountRegistrationRequestDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(AdminAccountController::class)
class AdminAccountControllerTest {

  @MockBean
  lateinit var accountService: AccountService
  @Autowired
  lateinit var mockMvc: MockMvc
  @Autowired
  lateinit var objectMapper: ObjectMapper

  @Test
  fun `context loads`() {}

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `register with no body should return - 400 Bad Request`() {
    mockMvc.perform(post("/admin/user")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(null)))
      .andExpect(status().isBadRequest)
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `register with body should return - 201 Created`() {
    val accountRegistrationRequestDto = AccountRegistrationRequestDto("username", "useremail", "password", 1)
    mockMvc.perform(post("/admin/user")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(accountRegistrationRequestDto)))
      .andExpect(status().isCreated)
  }

}