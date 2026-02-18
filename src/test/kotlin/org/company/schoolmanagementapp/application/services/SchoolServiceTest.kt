package org.company.schoolmanagementapp.application.services

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.company.schoolmanagementapp.TestDataProvider
import org.company.schoolmanagementapp.application.dtos.CreateOrUpdateSchoolRequestDto
import org.company.schoolmanagementapp.domain.SchoolEntity
import org.company.schoolmanagementapp.domain.StudentEntity
import org.company.schoolmanagementapp.infrastructure.persistence.SchoolRepository
import org.company.schoolmanagementapp.infrastructure.persistence.StudentRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import java.util.UUID
import kotlin.test.Test

@SpringBootTest
@ActiveProfiles("test")
class SchoolServiceTest {

    @Autowired
    lateinit var service: SchoolService

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

    // Internal self-contained flows can be tested with mocks.

    @Test
    fun `getSchools uses simple query to retrieve schools when name is null`() {
        val schoolRepository = mockk<SchoolRepository>()
        val studentRepository = mockk<StudentRepository>()
        val service = SchoolService(schoolRepository, studentRepository)

        val pageable = PageRequest.of(0, 10)
        val school = SchoolEntity(id = UUID.randomUUID(), name = "School", capacity = 500)

        every { schoolRepository.findAll(pageable) } returns PageImpl(listOf(school), pageable, 1)

        val result = service.getSchools(null, pageable)

        assertEquals(1, result.totalElements)
        assertEquals("School", result.content.single().name)

        verify(exactly = 1) { schoolRepository.findAll(pageable) }
        verify(exactly = 0) { schoolRepository.findByNameContainingIgnoreCase(any(), any()) }
    }

    @Test
    fun `getSchools uses query with filter to retrieve schools when name is provided`() {
        val schoolRepository = mockk<SchoolRepository>()
        val studentRepository = mockk<StudentRepository>()
        val service = SchoolService(schoolRepository, studentRepository)

        val pageable = PageRequest.of(0, 10)
        val school = SchoolEntity(id = UUID.randomUUID(), name = "Warsaw School No 1", capacity = 500)

        every { schoolRepository.findByNameContainingIgnoreCase("Warsaw", pageable) } returns PageImpl(listOf(school), pageable, 1)

        val result = service.getSchools("Warsaw", pageable)

        assertEquals(1, result.totalElements)
        assertEquals("Warsaw School No 1", result.content.single().name)

        verify(exactly = 0) { schoolRepository.findAll(any<Pageable>()) }
        verify(exactly = 1) { schoolRepository.findByNameContainingIgnoreCase("Warsaw", pageable) }
    }

    @Test
    fun `getSchoolDetails uses simple query to retrieve students when student name is null`() {
        val schoolRepository = mockk<SchoolRepository>()
        val studentRepository = mockk<StudentRepository>()
        val service = SchoolService(schoolRepository, studentRepository)

        val schoolId = UUID.randomUUID()
        val studentId = UUID.randomUUID()
        val pageable = PageRequest.of(0, 10)
        val school = SchoolEntity(id = schoolId, name = "School", capacity = 300)
        val student = StudentEntity(id = studentId, name = "John Doe")

        every { schoolRepository.findByIdOrNull(schoolId) } returns school
        every { studentRepository.findBySchoolId(schoolId, pageable) } returns PageImpl(listOf(student), pageable, 1)

        val result = service.getSchoolDetails(schoolId, null, pageable)

        assertEquals("School", result.name)
        assertEquals("John Doe", result.students.content.single().name)

        verify(exactly = 1) { studentRepository.findBySchoolId(schoolId, pageable) }
        verify(exactly = 0) { studentRepository.findBySchoolIdAndNameContainingIgnoreCase(any(), any(), any()) }
    }

    @Test
    fun `getSchoolDetails uses query with filter to retrieve students when student name is provided`() {
        val schoolRepository = mockk<SchoolRepository>()
        val studentRepository = mockk<StudentRepository>()
        val service = SchoolService(schoolRepository, studentRepository)

        val schoolId = UUID.randomUUID()
        val studentId = UUID.randomUUID()
        val pageable = PageRequest.of(0, 10)
        val school = SchoolEntity(id = schoolId, name = "School", capacity = 300)
        val student = StudentEntity(id = studentId, name = "John Doe")

        every { schoolRepository.findByIdOrNull(schoolId) } returns school
        every { studentRepository.findBySchoolIdAndNameContainingIgnoreCase(schoolId, "John", pageable) } returns PageImpl(listOf(student), pageable, 1)

        val result = service.getSchoolDetails(schoolId, "John", pageable)

        assertEquals("School", result.name)
        assertEquals("John Doe", result.students.content.single().name)

        verify(exactly = 0) { studentRepository.findBySchoolId(any(), any()) }
        verify(exactly = 1) { studentRepository.findBySchoolIdAndNameContainingIgnoreCase(schoolId, "John", pageable) }
    }

    // etc...

    // Testing with full context where more appropriate

    @Test
    fun `updateSchool modifies information in database and returns updated school info`() {
        val schoolId = UUID.randomUUID()

        val initialDbRecord = schoolRepository.save(
            SchoolEntity(id = schoolId, name = "School", capacity = 1)
        )

        // Verify initial state
        with (initialDbRecord!!) {
            assertEquals(schoolId, id)
            assertEquals( "School", name)
            assertEquals(1, capacity)
        }

        // Verify response
        val response = service.updateSchool(
            schoolId,
            CreateOrUpdateSchoolRequestDto(name = "Updated School", capacity = 2)
        )
        with (response) {
            assertEquals(schoolId, id)
            assertEquals("Updated School", name)
            assertEquals(2, capacity)
        }

        // Verify database
        val updatedDbRecord = schoolRepository.findByIdOrNull(schoolId)
        with (updatedDbRecord!!) {
            assertEquals(schoolId, id)
            assertEquals("Updated School", name)
            assertEquals(2, capacity)
        }
    }

    @Test
    fun `deleteSchool removes school and unassigns students`() {
        val schoolId = UUID.randomUUID()

        // Create school
        val school = schoolRepository.save(
            SchoolEntity(id = schoolId, name = "School", capacity = 1)
        )
        assertEquals(1, schoolRepository.findAll().size)

        // Create student assigned to school
        val studentId = UUID.randomUUID()
        studentRepository.save(StudentEntity(id = studentId, name = "John Doe", school))

        // Delete school
        service.deleteSchool(schoolId)

        // Verify there are no schools left
        assertEquals(0, schoolRepository.findAll().size)

        // Verify the student is no longer assigned to any school
        val updatedStudent = studentRepository.findByIdOrNull(studentId)!!
        assertEquals(null, updatedStudent.school)
    }

    // etc...
}

