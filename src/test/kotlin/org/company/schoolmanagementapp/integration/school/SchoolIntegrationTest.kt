package org.company.schoolmanagementapp.integration.school

import com.jayway.jsonpath.JsonPath
import org.company.schoolmanagementapp.application.dtos.CreateOrUpdateSchoolRequestDto
import org.company.schoolmanagementapp.integration.BaseIntegrationTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import kotlin.test.Test

class SchoolIntegrationTest: BaseIntegrationTest() {

    @Test
    fun `verify that user can create schools and see the list of created schools`() {
        // Two schools to be created
        val requestA = CreateOrUpdateSchoolRequestDto(name = "Test School A", capacity = 100)
        val requestB = CreateOrUpdateSchoolRequestDto(name = "Test School B", capacity = 50)

        // Create School A
        mockMvc.post("/schools") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestA)
        }.andExpect {
            status { isCreated() }
        }

        // Create School B
        mockMvc.post("/schools") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestB)
        }.andExpect {
            status { isCreated() }
        }

        // Get the list of all schools
        mockMvc.get("/schools")
            .andExpect {
                status { isOk() }

                jsonPath("$.content.length()") { value(2) }
                jsonPath("$.totalElements") { value(2) }
                jsonPath("$.totalPages") { value(1) }

                jsonPath("$.content[0].name") { value("Test School A") }
                jsonPath("$.content[0].capacity") { value(100) }

                jsonPath("$.content[1].name") { value("Test School B") }
                jsonPath("$.content[1].capacity") { value(50) }
            }
    }

    @Test
    fun `verify that user can update created school`() {
        // Create school
        val postRequest = CreateOrUpdateSchoolRequestDto(name = "Test School", capacity = 100)

        val postResponse = mockMvc.post("/schools") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(postRequest)
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        val schoolId: String = JsonPath.read(
            postResponse.response.contentAsString,
            "$.id"
        )

        // Update school's capacity
        val putRequest = CreateOrUpdateSchoolRequestDto(name = "Test School", capacity = 50)

        mockMvc.put("/schools/$schoolId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(putRequest)
        }.andExpect {
            status { isOk() }
        }

        // Get the list of all schools with a single updated instance
        mockMvc.get("/schools")
            .andExpect {
                status { isOk() }

                jsonPath("$.content.length()") { value(1) }
                jsonPath("$.totalElements") { value(1) }
                jsonPath("$.totalPages") { value(1) }

                jsonPath("$.content[0].name") { value("Test School") }
                jsonPath("$.content[0].capacity") { value(50) }
            }
    }

    // etc...
    // Typical flows of user interaction with app should be preferably tested in a similar fashion
    // and remain separated from implementation + serve as documentation of business logic
}