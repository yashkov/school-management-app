package org.company.schoolmanagementapp.interfaces.rest

import org.company.schoolmanagementapp.application.dtos.schools.CreateOrUpdateSchoolRequestDto
import org.company.schoolmanagementapp.application.dtos.PageResponse
import org.company.schoolmanagementapp.application.dtos.schools.SchoolBasicResponseDto
import org.company.schoolmanagementapp.application.exceptions.SchoolNotFoundException
import org.company.schoolmanagementapp.application.services.SchoolService
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import tools.jackson.databind.ObjectMapper
import java.util.*
import kotlin.test.Test

@WebMvcTest(SchoolController::class)
class SchoolControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var schoolService: SchoolService

    @Test
    fun `GET schools should return 200 with list of schools`() {
        val pageable: Pageable = PageRequest.of(0, 20)
        val sampleSchools = listOf(
            SchoolBasicResponseDto(UUID.randomUUID(), "Test School 1", 5),
            SchoolBasicResponseDto(UUID.randomUUID(), "Test School 2", 5)
        )
        val page = PageResponse(
            content = sampleSchools,
            page = 0,
            size = 20,
            totalElements = sampleSchools.size.toLong(),
            totalPages = 1)

        `when`(schoolService.getSchools(null, pageable)).thenReturn(page)

        mockMvc.get("/schools") {
                param("page", "0")
                param("size", "20")
                accept(MediaType.APPLICATION_JSON)
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.content.length()") { value(2) }
            }

        verify(schoolService, times(1)).getSchools(null, pageable)
    }

    @Test
    fun `POST schools should return 422 when capacity is below required minimum`() {
        val request = CreateOrUpdateSchoolRequestDto(name = "Invalid School", capacity = 20)

        mockMvc.post("/schools") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isUnprocessableContent() }
        }

        verifyNoInteractions(schoolService)
    }

    @Test
    fun `PUT schools should return 422 when capacity is above acceptable maximum`() {
        val request = CreateOrUpdateSchoolRequestDto(name = "Invalid School", capacity = 5000)

        mockMvc.put("/schools/${UUID.randomUUID()}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isUnprocessableContent() }
        }

        verifyNoInteractions(schoolService)
    }

    @Test
    fun `GET school details returns 404 when school is not found`() {
        val id = UUID.randomUUID()
        val pageable: Pageable = PageRequest.of(0, 20)
        `when`(
            schoolService.getSchoolDetails(
                id,
                null,
                pageable
            )
        ).thenThrow(
            SchoolNotFoundException()
        )

        mockMvc.get("/schools/$id") {
            param("page", "0")
            param("size", "20")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.detail") { value("School not found") }
        }
    }

    // etc...
    // Controller-level tests can use mocking and should mostly verify the use of correct response codes,
    // user-facing messages for exceptions, handling of authorization, etc.
}
