package org.company.schoolmanagementapp.integration

import org.company.schoolmanagementapp.application.dtos.cases.CaseResponseDto
import org.company.schoolmanagementapp.application.dtos.cases.CreateCaseRequestDto
import org.company.schoolmanagementapp.application.dtos.cases.UpdateCaseRequestDto
import org.company.schoolmanagementapp.application.dtos.schools.CreateOrUpdateSchoolRequestDto
import org.company.schoolmanagementapp.application.dtos.schools.SchoolBasicResponseDto
import org.company.schoolmanagementapp.application.dtos.students.CreateOrUpdateStudentRequestDto
import org.company.schoolmanagementapp.application.dtos.students.StudentBasicResponseDto
import org.company.schoolmanagementapp.application.dtos.students.StudentDetailsResponseDto
import org.company.schoolmanagementapp.domain.enums.CaseStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import kotlin.test.Test
import kotlin.test.assertEquals

class CaseIntegrationTest : BaseIntegrationTest() {

    @Test
    fun `verify that user can create a case, progress it to approval, and confirm student's assignment`() {

        // School and a student setup
        val schoolResponse = mockMvc.post("/schools") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                CreateOrUpdateSchoolRequestDto(name = "School", capacity = 100)
            )
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        val createdSchool = readResponse(
            schoolResponse.response.contentAsString,
            SchoolBasicResponseDto::class.java
        )

        val studentResponse = mockMvc.post("/students") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                CreateOrUpdateStudentRequestDto(name = "Student")
            )
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        val createdStudent = readResponse(
            studentResponse.response.contentAsString,
            StudentBasicResponseDto::class.java
        )

        // Case creation
        val caseResponse = mockMvc.post("/cases") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                CreateCaseRequestDto(
                    studentId = createdStudent.id,
                    schoolId = createdSchool.id
                )
            )
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        val createdCase = readResponse(
            caseResponse.response.contentAsString,
            CaseResponseDto::class.java
        )
        assertEquals(CaseStatus.PENDING, createdCase.status)

        // Case visible when retrieving a full list
        val casesResponseBeforeApproval = mockMvc.get("/cases") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val casesBeforeApproval = readPageResponse(
            casesResponseBeforeApproval.response.contentAsString,
            CaseResponseDto::class.java
        )

        assertEquals(1, casesBeforeApproval.content.size)
        assertEquals(1L, casesBeforeApproval.totalElements)
        assertEquals(createdCase.caseId, casesBeforeApproval.content[0].caseId)
        assertEquals(CaseStatus.PENDING, casesBeforeApproval.content[0].status)

        // Case approved
        mockMvc.put("/cases/${createdCase.caseId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                UpdateCaseRequestDto(status = CaseStatus.APPROVED)
            )
        }.andExpect {
            status { isOk() }
        }

        // Case status change visible when retrieving a full list
        val casesResponseAfterApproval = mockMvc.get("/cases") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val casesAfterApproval = readPageResponse(
            casesResponseAfterApproval.response.contentAsString,
            CaseResponseDto::class.java
        )

        assertEquals(1, casesAfterApproval.content.size)
        assertEquals(CaseStatus.APPROVED, casesAfterApproval.content[0].status)

        // Student assigned to school
        val updatedStudentResponse = mockMvc.get("/students/${createdStudent.id}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val updatedStudent = readResponse(
            updatedStudentResponse.response.contentAsString,
            StudentDetailsResponseDto::class.java
        )

        assertEquals(createdSchool.id, updatedStudent.schoolId)
        assertEquals(createdSchool.name, updatedStudent.schoolName)
    }

    // etc...
}
