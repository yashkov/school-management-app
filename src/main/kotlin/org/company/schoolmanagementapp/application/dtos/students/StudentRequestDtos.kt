package org.company.schoolmanagementapp.application.dtos.students

import java.util.UUID

data class CreateOrUpdateStudentRequestDto(
    val name: String
)

data class AssignStudentRequestDto(
    val schoolId: UUID?
)