package org.company.schoolmanagementapp.application.dtos

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