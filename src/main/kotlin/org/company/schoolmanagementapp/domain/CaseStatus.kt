package org.company.schoolmanagementapp.domain

enum class CaseStatus {
    PENDING, APPROVED, REJECTED;

    // Exemplary flow constraints
    fun getAllowedProgression(): List<CaseStatus> {
        return when (this) {
            PENDING -> listOf(APPROVED, REJECTED)
            APPROVED -> emptyList()
            REJECTED -> listOf(PENDING)
        }
    }
}