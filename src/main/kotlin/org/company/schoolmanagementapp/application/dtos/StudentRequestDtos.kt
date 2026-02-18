package org.company.schoolmanagementapp.application.dtos

import java.util.UUID

data class CreateOrUpdateStudentRequestDto(
    val name: String
)

data class AssignStudentRequestDto(
    val schoolId: UUID?
)