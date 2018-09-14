package org.dailyepisode.controller.admin

import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.subscription.SubscriptionService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AdminSubscriptionControllerTest: AbstractControllerIntegrationTest() {

  @Autowired
  private lateinit var subscriptionService: SubscriptionService

  @Test
  fun `user role access for user without admin privileges should return 403 Forbidden`() {
    mockMvc.perform(get("/admin/subscription")
      .with(csrf())
      .with(httpBasic("kristoffer","reffotsirk"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden)
  }

  @Test
  fun `get all subscriptions should return all subscriptions and 200 Ok`() {
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
       "numberOfSeasons":6},
      {"remoteId":3,
       "name":"line of duty",
       "overview":"Corrupt police investigations",
       "imageUrl":"image",
       "voteCount":6,
       "voteAverage":7.5,
       "firstAirDate":"2009-05-18",
       "lastAirDate":"2017-02-29",
       "genres":["Thriller","Crime","Drama"],
       "homepage":"www.lineofduty.com",
       "numberOfEpisodes":48,
       "numberOfSeasons":5}
      ]""".trimIndent()

    mockMvc.perform(get("/admin/subscription")
      .with(csrf())
      .with(httpBasic("patrik","kirtap"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  fun `get subscription with existing id should return subscription and 200 Ok`() {
    val gameOfThrones = subscriptionService.findByRemoteId(1)!!

    val expectedJson = """
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
       "numberOfSeasons":8}
      """.trimIndent()

    mockMvc.perform(get("/admin/subscription/${gameOfThrones.id}")
      .with(csrf())
      .with(httpBasic("patrik","kirtap"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson))
  }

  @Test
  fun `get subscription with non-existing id should return 204 No Content`() {
    mockMvc.perform(get("/admin/subscription/666")
      .with(csrf())
      .with(httpBasic("patrik","kirtap"))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent)
  }

}