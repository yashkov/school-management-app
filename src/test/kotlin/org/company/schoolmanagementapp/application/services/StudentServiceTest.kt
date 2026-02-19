package org.company.schoolmanagementapp.application.services

import org.company.schoolmanagementapp.TestDataProvider
import org.company.schoolmanagementapp.application.dtos.AssignStudentRequestDto
import org.company.schoolmanagementapp.domain.SchoolEntity
import org.company.schoolmanagementapp.domain.StudentEntity
import org.company.schoolmanagementapp.infrastructure.persistence.SchoolRepository
import org.company.schoolmanagementapp.infrastructure.persistence.StudentRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*
import kotlin.test.Test

@SpringBootTest
@ActiveProfiles("test")
class StudentServiceTest {

    @Autowired
    lateinit var service: StudentService

    @Autowired
    lateinit var schoolRepository: SchoolRepository

    @Autowired
    lateinit var studentRepository: StudentRepository

    @Autowired
    lateinit var testDataProvider: TestDataProvider

    @AfterEach
    fun afterEach() {
        testDataProvider.cleanDatabase()
    }

    // As in SchoolServiceTest: verification of basic flows with mocking + testing with full context for more advanced scenarios

    @Test
    fun `assignStudentToSchool does not allow to assign student once capacity is reached`() {
        val requestedSchoolId = UUID.randomUUID()
        val school = schoolRepository.save(
            SchoolEntity(id = requestedSchoolId, name = "School", capacity = 50)
        )

        // Hit minimum allowed capacity
        repeat(49) { index ->
            studentRepository.save(StudentEntity(name = "Preassigned $index", school = school))
        }

        val lastAcceptedStudent = UUID.randomUUID()
        val extraStudentToBeRejected = UUID.randomUUID()

        studentRepository.save(StudentEntity(id = lastAcceptedStudent, name = "John Doe"))
        studentRepository.save(StudentEntity(id = extraStudentToBeRejected, name = "Jane Doe"))

        val assignStudentRequest = AssignStudentRequestDto(schoolId = requestedSchoolId)

        val firstAssignmentResult = service.assignStudentToSchool(lastAcceptedStudent, assignStudentRequest)

        with (firstAssignmentResult) {
            assertEquals(lastAcceptedStudent, id)
            assertEquals("John Doe", name)
            assertEquals(requestedSchoolId, schoolId)
            assertEquals(school.name, schoolName)
        }

        val exception = assertThrows(RuntimeException::class.java) {
            service.assignStudentToSchool(extraStudentToBeRejected, assignStudentRequest)
        }

        assertEquals("Cannot be assigned to school, school is full", exception.message)
    }

    // etc...
}
