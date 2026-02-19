package org.company.schoolmanagementapp.domain.enums

enum class CaseStatus {
    PENDING, APPROVED, REJECTED;

    // Exemplary flow constraints
    fun getAllowedProgression(): List<CaseStatus> {
        return when (this) {
            PENDING -> listOf(APPROVED, REJECTED)
            APPROVED -> emptyList()     // an approved case is final
            REJECTED -> listOf(PENDING) // a rejected case can be reopened
        }
    }
}