package org.company.schoolmanagementapp.application.dtos

import org.company.schoolmanagementapp.domain.CaseStatus
import java.util.UUID

data class CreateCaseRequestDto(
    val studentId: UUID,
    val schoolId: UUID,
)

data class UpdateCaseRequestDto(
    val status: CaseStatus
)