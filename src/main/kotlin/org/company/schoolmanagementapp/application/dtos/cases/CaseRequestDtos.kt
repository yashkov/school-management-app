package org.company.schoolmanagementapp.application.dtos.cases

import org.company.schoolmanagementapp.domain.enums.CaseStatus
import java.util.UUID

data class CreateCaseRequestDto(
    val studentId: UUID,
    val schoolId: UUID,
)

data class UpdateCaseRequestDto(
    val status: CaseStatus
)