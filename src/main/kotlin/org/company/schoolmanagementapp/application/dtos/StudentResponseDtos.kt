package org.company.schoolmanagementapp.application.dtos

import java.util.UUID

data class StudentBasicResponseDto(
    val id: UUID,
    val name: String
)

data class StudentDetailsResponseDto(
    val id: UUID,
    val name: String,
    val schoolId: UUID?,
    val schoolName: String?
)