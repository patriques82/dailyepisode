package org.dailyepisode.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.dailyepisode.Application
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(Application::class))
@TestPropertySource("classpath:integrationtest.properties")
@Transactional // Roll back database after each test method
abstract class AbstractControllerIntegrationTest {

  @Autowired
  protected lateinit var objectMapper: ObjectMapper
  @Autowired
  private lateinit var context: WebApplicationContext
  @Autowired
  private lateinit var springSecurityFilterChain: FilterChainProxy

  protected lateinit var mockMvc: MockMvc

  @Before
  fun setUp() {
    mockMvc = MockMvcBuilders
      .webAppContextSetup(context)
      .apply<DefaultMockMvcBuilder>(springSecurity(springSecurityFilterChain))
      .build()
  }

}