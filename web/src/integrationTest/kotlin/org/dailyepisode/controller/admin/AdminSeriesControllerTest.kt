package org.dailyepisode.controller.admin

import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.series.SeriesService
import org.dailyepisode.series.SeriesUpdateResult
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AdminSeriesControllerTest: AbstractControllerIntegrationTest() {

  @MockBean
  private lateinit var seriesService: SeriesService

  @Test
  @WithMockUser(roles = arrayOf("USER"))
  fun `user role access should return 403 Forbidden`() {
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/series/changes")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden)
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `changes should return latest changes and 200 Ok`() {
    given(seriesService.updatesSinceYesterday()).willReturn(SeriesUpdateResult(listOf(1,2,3)))

    val expectedJson = """
      {"changedSeriesRemoteIds": [1,2,3]}
    """.trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/series/changes")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson, true))
  }
}

