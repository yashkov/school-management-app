package org.company.schoolmanagementapp.integration

import org.company.schoolmanagementapp.TestDataProvider
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import tools.jackson.databind.ObjectMapper

@SpringBootTest
@ActiveProfiles("test")
abstract class BaseIntegrationTest {

    @Autowired
    protected lateinit var testDataProvider: TestDataProvider

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    protected lateinit var mockMvc: MockMvc

    @BeforeEach
    fun beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build()
    }

    @AfterEach
    fun afterEach() {
        testDataProvider.cleanDatabase()
    }
}