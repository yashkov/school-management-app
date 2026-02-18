package org.company.schoolmanagementapp.integration.school

import org.company.schoolmanagementapp.application.dtos.CreateOrUpdateSchoolRequestDto
import org.company.schoolmanagementapp.application.dtos.PageResponse
import org.company.schoolmanagementapp.application.dtos.SchoolBasicResponseDto
import org.company.schoolmanagementapp.integration.BaseIntegrationTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import kotlin.test.Test
import kotlin.test.assertEquals

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
        val schoolsResponse = mockMvc.get("/schools")
            .andExpect {
                status { isOk() }
            }
            .andReturn()

        val schoolsType = objectMapper.typeFactory.constructParametricType(
            PageResponse::class.java,
            SchoolBasicResponseDto::class.java
        )
        val schools = objectMapper.readValue<PageResponse<SchoolBasicResponseDto>>(
            schoolsResponse.response.contentAsString,
            schoolsType
        )

        assertEquals(2, schools.content.size)
        assertEquals(2L, schools.totalElements)
        assertEquals(1, schools.totalPages)

        with (schools.content[0]) {
            assertEquals("Test School A", name)
            assertEquals(100, capacity)
        }
        with (schools.content[1]) {
            assertEquals("Test School B", name)
            assertEquals(50, capacity)
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

        val createdSchool = objectMapper.readValue(
            postResponse.response.contentAsString,
            SchoolBasicResponseDto::class.java
        )

        // Update school's capacity
        val putRequest = CreateOrUpdateSchoolRequestDto(name = "Test School", capacity = 50)

        mockMvc.put("/schools/${createdSchool.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(putRequest)
        }.andExpect {
            status { isOk() }
        }

        // Get the list of all schools with a single updated instance
        val schoolsResponse = mockMvc.get("/schools")
            .andExpect {
                status { isOk() }
            }
            .andReturn()

        val schoolsType = objectMapper.typeFactory.constructParametricType(
            PageResponse::class.java,
            SchoolBasicResponseDto::class.java
        )
        val schools = objectMapper.readValue<PageResponse<SchoolBasicResponseDto>>(
            schoolsResponse.response.contentAsString,
            schoolsType
        )

        assertEquals(1, schools.content.size)
        assertEquals(1L, schools.totalElements)
        assertEquals(1, schools.totalPages)
        assertEquals("Test School", schools.content[0].name)
        assertEquals(50, schools.content[0].capacity)
    }

    // etc...
    // Typical flows of user interaction with app should be preferably tested in a similar fashion
    // and remain separated from implementation + serve as documentation of business logic
}
