package org.company.schoolmanagementapp.integration

import org.springframework.test.web.servlet.get
import kotlin.test.Test

class OpenApiDocumentationIntegrationTest : BaseIntegrationTest() {

    @Test
    fun `swagger ui and openapi docs are exposed`() {
        mockMvc.get("/swagger-ui/index.html")
            .andExpect {
                status { isOk() }
            }

        mockMvc.get("/v3/api-docs")
            .andExpect {
                status { isOk() }
            }
    }
}
