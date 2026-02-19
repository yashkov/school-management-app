package org.company.schoolmanagementapp.application.services

import org.company.schoolmanagementapp.application.dtos.*
import org.company.schoolmanagementapp.application.dtos.students.AssignStudentRequestDto
import org.company.schoolmanagementapp.application.dtos.students.CreateOrUpdateStudentRequestDto
import org.company.schoolmanagementapp.application.dtos.students.StudentBasicResponseDto
import org.company.schoolmanagementapp.application.dtos.students.StudentDetailsResponseDto
import org.company.schoolmanagementapp.application.exceptions.SchoolCapacityExceededException
import org.company.schoolmanagementapp.application.exceptions.SchoolNotFoundException
import org.company.schoolmanagementapp.application.exceptions.StudentNotFoundException
import org.company.schoolmanagementapp.domain.StudentEntity
import org.company.schoolmanagementapp.infrastructure.persistence.SchoolRepository
import org.company.schoolmanagementapp.infrastructure.persistence.StudentRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class StudentService(
    val studentRepository: StudentRepository,
    val schoolRepository: SchoolRepository
) {
    fun getStudents(name: String?, pageable: Pageable): PageResponse<StudentBasicResponseDto> {
        val studentsPage = if (name.isNullOrBlank()) {
            studentRepository.findAll(pageable)
        } else {
            studentRepository.findByNameContainingIgnoreCase(name, pageable)
        }
        return studentsPage.map { StudentBasicResponseDto(it.id, it.name) }.toPageResponse()
    }

    fun getStudentDetails(id: UUID): StudentDetailsResponseDto {
        val studentEntity = studentRepository.findByIdOrNull(id) ?: throw StudentNotFoundException()
        return StudentDetailsResponseDto(
            id = studentEntity.id,
            name = studentEntity.name,
            schoolId = studentEntity.school?.id,
            schoolName = studentEntity.school?.name
        )
    }

    @Transactional
    fun createStudent(createStudentRequest: CreateOrUpdateStudentRequestDto): StudentBasicResponseDto {
        val studentEntity = studentRepository.save(StudentEntity(name = createStudentRequest.name))
        return StudentBasicResponseDto(studentEntity.id, studentEntity.name)
    }

    @Transactional
    fun updateStudent(id: UUID, updateStudentRequest: CreateOrUpdateStudentRequestDto): StudentDetailsResponseDto {
        val studentEntity = studentRepository.findByIdOrNull(id) ?: throw StudentNotFoundException()
        studentEntity.name = updateStudentRequest.name

        return StudentDetailsResponseDto(
            id = studentEntity.id,
            name = studentEntity.name,
            schoolId = studentEntity.school?.id,
            schoolName = studentEntity.school?.name
        )
    }

    @Transactional
    fun assignStudentToSchool(id: UUID, assignStudentRequest: AssignStudentRequestDto): StudentDetailsResponseDto {
        val studentEntity = studentRepository.findByIdOrNull(id) ?: throw StudentNotFoundException()
        studentEntity.school = assignStudentRequest.schoolId?.let {
            val schoolEntity = schoolRepository.findByIdOrNull(assignStudentRequest.schoolId) ?: throw SchoolNotFoundException()

            if (schoolEntity.students.size >= schoolEntity.capacity) {
                throw SchoolCapacityExceededException()
            }
            schoolEntity
        }

        return StudentDetailsResponseDto(
            id = studentEntity.id,
            name = studentEntity.name,
            schoolId = studentEntity.school?.id,
            schoolName = studentEntity.school?.name
        )
    }

    @Transactional
    fun deleteStudent(id: UUID) {
        studentRepository.deleteById(id)
    }
}
