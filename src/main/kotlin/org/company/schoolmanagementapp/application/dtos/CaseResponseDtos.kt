package org.company.schoolmanagementapp.application.dtos

import org.company.schoolmanagementapp.domain.CaseStatus
import java.util.UUID

data class CaseResponseDto(
    val caseId: UUID,
    val studentId: UUID,
    val studentName: String,
    val schoolId: UUID,
    val schoolName: String,
    val status: CaseStatus
)