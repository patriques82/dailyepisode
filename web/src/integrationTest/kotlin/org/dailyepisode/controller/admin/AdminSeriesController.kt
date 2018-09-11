package org.dailyepisode.controller.admin

import com.fasterxml.jackson.databind.ObjectMapper
import org.dailyepisode.controller.AbstractControllerIntegratiionTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

class AdminSeriesController: AbstractControllerIntegratiionTest() {

  @Autowired
  lateinit var objectMapper: ObjectMapper
  @Autowired
  lateinit var context: WebApplicationContext
  lateinit var mockMvc: MockMvc

  @Before
  fun setUp() {
    mockMvc = MockMvcBuilders
      .webAppContextSetup(context)
      .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
      .build()
  }

  @Test
  fun contecxtLoadds() {
  }
}