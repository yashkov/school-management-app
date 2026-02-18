package org.company.schoolmanagementapp

import org.company.schoolmanagementapp.infrastructure.persistence.SchoolRepository
import org.company.schoolmanagementapp.infrastructure.persistence.StudentRepository
import org.springframework.stereotype.Component

@Component
class TestDataProvider(
    val schoolRepository: SchoolRepository,
    val studentRepository: StudentRepository
) {

    fun cleanDatabase() {
        schoolRepository.deleteAll()
        studentRepository.deleteAll()
    }

    // Any seeds for testing purposes -> add here
}