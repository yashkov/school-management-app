package org.company.schoolmanagementapp.interfaces.rest

import org.company.schoolmanagementapp.application.dtos.SchoolBasicResponseDto
import org.company.schoolmanagementapp.application.services.SchoolService
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.UUID
import kotlin.test.Test

@WebMvcTest(SchoolController::class)
class SchoolControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var schoolService: SchoolService

    @Test
    fun `GET schools should return 200 with list of schools`() {
        val pageable: Pageable = PageRequest.of(0, 20)
        val sampleSchools = listOf(
            SchoolBasicResponseDto(UUID.randomUUID(), "Test School 1", 5),
            SchoolBasicResponseDto(UUID.randomUUID(), "Test School 2", 5)
        )
        val page = PageImpl(sampleSchools, pageable, sampleSchools.size.toLong())

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

    // etc...
    // Controller-level tests can use mocking and should mostly verify the use of correct response codes,
    // user-facing messages for exceptions, handling of authorization, etc.
}