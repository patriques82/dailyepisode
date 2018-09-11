package org.dailyepisode.controller.admin

import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.subscription.SubscriptionEntity
import org.dailyepisode.subscription.SubscriptionRepository
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class AdminSubscriptionControllerTest: AbstractControllerIntegrationTest() {

  @MockBean
  private lateinit var subscriptionRepository: SubscriptionRepository

  @Test
  @WithMockUser(roles = arrayOf("USER"))
  fun `user role access should return 403 Forbidden`() {
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/subscription")
      .with(SecurityMockMvcRequestPostProcessors.csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isForbidden)
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `get all subscriptions should return all subscriptions and 200 Ok`() {
    val gameOfThrones = SubscriptionEntity(1, 1, "game of thrones", "Winter is coming...", "image",
      10, 8.6, "2013-05-15", "2018-05-16", listOf("Fantasy", "Drama"),
      "www.got.com", 72, 8, emptyList())
    val breakingBad = SubscriptionEntity(2, 2, "breaking bad", "Meth dealing tutorial", "image", 29,
      9.1, "2010-01-01", "2014-06-01", listOf("Thriller", "Crime", "Drama"),
      "www.breakingbad.com", 55, 6, emptyList())
    given(subscriptionRepository.findAll()).willReturn(listOf(gameOfThrones, breakingBad))

    val expectedJson = """[
      {"remoteId":1,
       "name":"game of thrones",
       "overview":"Winter is coming...",
       "imageUrl":"image",
       "voteCount":10,
       "voteAverage":8.6,
       "firstAirDate":"2013-05-15",
       "lastAirDate":"2018-05-16",
       "genres":["Fantasy","Drama"],
       "homepage":"www.got.com",
       "numberOfEpisodes":72,
       "numberOfSeasons":8},
      {"remoteId":2,
       "name":"breaking bad",
       "overview":"Meth dealing tutorial",
       "imageUrl":"image",
       "voteCount":29,
       "voteAverage":9.1,
       "firstAirDate":"2010-01-01",
       "lastAirDate":"2014-06-01",
       "genres":["Thriller","Crime","Drama"],
       "homepage":"www.breakingbad.com",
       "numberOfEpisodes":55,
       "numberOfSeasons":6}
      ]""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/subscription")
      .with(SecurityMockMvcRequestPostProcessors.csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `get subscription with existing id should return subscription and 200 Ok`() {
    val breakingBad = SubscriptionEntity(1, 2, "breaking bad", "Meth dealing tutorial", "image", 29,
      9.1, "2010-01-01", "2014-06-01", listOf("Thriller", "Crime", "Drama"),
      "www.breakingbad.com", 55, 6, emptyList())
    given(subscriptionRepository.findById(1)).willReturn(Optional.of(breakingBad))

    val expectedJson = """
      {"remoteId":2,
       "name":"breaking bad",
       "overview":"Meth dealing tutorial",
       "imageUrl":"image",
       "voteCount":29,
       "voteAverage":9.1,
       "firstAirDate":"2010-01-01",
       "lastAirDate":"2014-06-01",
       "genres":["Thriller","Crime","Drama"],
       "homepage":"www.breakingbad.com",
       "numberOfEpisodes":55,
       "numberOfSeasons":6}
      """.trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/subscription/1")
      .with(SecurityMockMvcRequestPostProcessors.csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  @WithMockUser(roles = arrayOf("ADMIN"))
  fun `get subscription with non-existing id should return 204 No Content`() {
    given(subscriptionRepository.findById(1)).willReturn(Optional.empty())

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/subscription/1")
      .with(SecurityMockMvcRequestPostProcessors.csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent)
  }

}