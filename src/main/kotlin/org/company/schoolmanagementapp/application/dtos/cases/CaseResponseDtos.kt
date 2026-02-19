package org.company.schoolmanagementapp.application.dtos.cases

import org.company.schoolmanagementapp.domain.enums.CaseStatus
import java.util.UUID

data class CaseResponseDto(
    val caseId: UUID,
    val studentId: UUID,
    val studentName: String,
    val schoolId: UUID,
    val schoolName: String,
    val status: CaseStatus
)