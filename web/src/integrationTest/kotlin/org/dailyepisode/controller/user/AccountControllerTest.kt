package org.dailyepisode.controller.user

import org.assertj.core.api.Assertions.assertThat
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.account.PasswordUpdateRequest
import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.dto.AccountUpdateRequestDto
import org.dailyepisode.dto.PasswordChangeRequestDto
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AccountControllerTest: AbstractControllerIntegrationTest() {

  @Autowired
  private lateinit var accountStorageService: AccountStorageService
  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder

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
      {"username":"alexia","email":"alexia@gmail.com","notificationIntervalInDays":9}
    """.trimIndent()

    mockMvc.perform(get("/api/user/me")
      .with(csrf())
      .with(httpBasic("alexia", "aixela"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  fun `update preferences with invalid notificationIntervalInDays should return 400 Bad Request`() {
    val alexia = accountStorageService.findByUserName("alexia")!!
    val invalidPreferencesRequestDto = AccountUpdateRequestDto(alexia.id, "alexia", -1)

    mockMvc.perform(put("/api/user/update")
      .with(csrf())
      .with(httpBasic("alexia", "aixela"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(invalidPreferencesRequestDto)))
      .andExpect(status().isBadRequest)
  }

  @Test
  fun `update preferences with invalid account id should return 400 Bad Request`() {
    val alexia = accountStorageService.findByUserName("alexia")!!
    val invalidPreferencesRequestDto = AccountUpdateRequestDto(alexia.id + 1,"alle", 1)

    mockMvc.perform(put("/api/user/update")
      .with(csrf())
      .with(httpBasic("alexia", "aixela"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(invalidPreferencesRequestDto)))
      .andExpect(status().isBadRequest)
  }

  @Test
  fun `update preferences with existing username should return 400 Bad Request`() {
    val alexia = accountStorageService.findByUserName("alexia")!!
    val invalidPreferencesRequestDto = AccountUpdateRequestDto(alexia.id,"kristoffer", 1)

    mockMvc.perform(put("/api/user/update")
      .with(csrf())
      .with(httpBasic("alexia", "aixela"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(invalidPreferencesRequestDto)))
      .andExpect(status().isBadRequest)
  }

  @Test
  fun `update preferences with valid data should return 201 Created`() {
    val kristofferBefore = accountStorageService.findByUserName("kristoffer")!!
    val preferencesRequestDto = AccountUpdateRequestDto(kristofferBefore.id, "krille", 3)

    assertThat(kristofferBefore.notificationIntervalInDays).isEqualTo(30)

    mockMvc.perform(put("/api/user/update")
      .with(csrf())
      .with(httpBasic("kristoffer", "reffotsirk"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(preferencesRequestDto)))
      .andExpect(status().isCreated)

    val kristofferAfter = accountStorageService.findByUserName("krille")!!
    assertThat(kristofferAfter.notificationIntervalInDays).isEqualTo(3)
  }

  @Test
  fun `update password with invalid password should return 400 Bad Request`() {
    val kristofferBefore = accountStorageService.findByUserName("kristoffer")!!
    assertThat(passwordEncoder.matches("reffotsirk", kristofferBefore.password)).isEqualTo(true)
    val passwordUpdateRequestDto = PasswordUpdateRequest(kristofferBefore.id, "Invalid_password")

    mockMvc.perform(put("/api/user/change-password")
      .with(csrf())
      .with(httpBasic("kristoffer", "reffotsirk"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(passwordUpdateRequestDto)))
      .andExpect(status().isBadRequest)
  }

  @Test
  fun `update password with valid password should return 201 Created`() {
    val kristofferBefore = accountStorageService.findByUserName("kristoffer")!!
    assertThat(passwordEncoder.matches("reffotsirk", kristofferBefore.password)).isEqualTo(true)

    val passwordUpdateRequestDto = PasswordChangeRequestDto(kristofferBefore.id, "newpassword")

    mockMvc.perform(put("/api/user/change-password")
      .with(csrf())
      .with(httpBasic("kristoffer", "reffotsirk"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(passwordUpdateRequestDto)))
      .andExpect(status().isCreated)

    val kristofferAfter = accountStorageService.findByUserName("kristoffer")!!
    assertThat(passwordEncoder.matches("newpassword", kristofferAfter.password)).isEqualTo(true)
  }

}