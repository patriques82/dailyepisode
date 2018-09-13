package org.dailyepisode.controller.user

import org.assertj.core.api.Assertions.assertThat
import org.dailyepisode.account.AccountService
import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.dto.SubscriptionRequestDto
import org.dailyepisode.series.TheMovieDBConnector
import org.dailyepisode.series.TheMovieDBLookupResult
import org.dailyepisode.series.TheMovieDbGenre
import org.dailyepisode.subscription.SubscriptionService
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class SubscriptionControllerTest : AbstractControllerIntegrationTest() {

  @MockBean
  private lateinit var theMovieDBConnector: TheMovieDBConnector

  @Autowired
  private lateinit var accountService: AccountService

  @Autowired
  private lateinit var subscriptionService: SubscriptionService

  @Test
  @WithMockUser(username = "alexia")
  fun `create subscription with invalid subscription data should return 400 Bad Request`() {
    mockMvc.perform(post("/api/subscription")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(null)))
      .andExpect(status().isBadRequest)
  }

  @Test
  @WithMockUser(username = "alexia")
  fun `create subscription with valid subscription data should return 201 Created`() {
    val rickAndMorty = TheMovieDBLookupResult(4, "rick and morty", "space explorers", "image",
      6, 7.5, "2009-05-18", "2017-02-29", listOf(TheMovieDbGenre("Comedy"), TheMovieDbGenre("Sci-fi")),
      "www.rickandmorty.com", 60, 5)
    given(theMovieDBConnector.fetchLookupResult(4)).willReturn(rickAndMorty)

    val subscriptionRequestDto = SubscriptionRequestDto(listOf(1, 2, 4)) // game of thrones, breaking bad, rick and morty
    mockMvc.perform(post("/api/subscription")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(subscriptionRequestDto)))
      .andExpect(status().isCreated)

    val alexia = accountService.findByUserName("alexia")
    assertThat(alexia).isNotNull
    val subscriptions = alexia!!.subscriptions
    assertThat(subscriptions.size).isEqualTo(3)
    assertThat(subscriptions[0].name).isEqualTo("game of thrones")
    assertThat(subscriptions[1].name).isEqualTo("breaking bad")
    assertThat(subscriptions[2].name).isEqualTo("rick and morty")
  }

  @Test
  @WithMockUser(username = "kristoffer")
  fun `get subscriptions should return account subscriptions and 200 Ok`() {
    val expectedJson = """
      [{"id":5,
        "remoteId":3,
        "name":"line of duty",
        "overview":"corrupt police",
        "imageUrl":"image",
        "voteCount":6,
        "voteAverage":7.5,
        "firstAirDate":"2009-05-18",
        "lastAirDate":"2017-02-29",
        "genres":["Crime","Drama"],
        "homepage":"www.lineofduty.com",
        "numberOfEpisodes":48,
        "numberOfSeasons":5},
       {"id":8,
        "remoteId":1,
        "name":"game of thrones",
        "overview":"winter is coming",
        "imageUrl":"image",
        "voteCount":10,
        "voteAverage":8.6,
        "firstAirDate":"2013-05-15",
        "lastAirDate":"2018-05-16",
        "genres":["Fantasy","Drama"],
        "homepage":"www.got.com",
        "numberOfEpisodes":72,
        "numberOfSeasons":8}]
    """.trimIndent()

    mockMvc.perform(get("/api/subscription")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson, true))
      .andReturn()
  }

  @Test
  @WithMockUser(username = "alexia")
  fun `remove subscription with non-existing account subscription id should return 409 Conflict`() {
    mockMvc.perform(delete("/api/subscription/1")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isConflict)
  }

  @Test
  @WithMockUser(username = "kristoffer")
  fun `remove subscription with existing account subscription id should return 200 Ok`() {
    val lineOfDuty = subscriptionService.findByRemoteId(3)

    mockMvc.perform(delete("/api/subscription/${lineOfDuty?.id}")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
  }
}