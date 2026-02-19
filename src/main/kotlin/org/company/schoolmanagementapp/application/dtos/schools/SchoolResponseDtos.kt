package org.company.schoolmanagementapp.application.dtos.schools

import org.company.schoolmanagementapp.application.dtos.PageResponse
import org.company.schoolmanagementapp.application.dtos.students.StudentBasicResponseDto
import java.util.UUID

data class SchoolBasicResponseDto(
    val id: UUID,
    val name: String,
    val capacity: Int
)

data class SchoolDetailsResponseDto(
    val id: UUID,
    val name: String,
    val capacity: Int,
    val students: PageResponse<StudentBasicResponseDto>
)