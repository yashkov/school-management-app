package org.company.schoolmanagementapp.application.services

import org.company.schoolmanagementapp.application.dtos.*
import org.company.schoolmanagementapp.application.dtos.cases.CaseResponseDto
import org.company.schoolmanagementapp.application.dtos.cases.CreateCaseRequestDto
import org.company.schoolmanagementapp.application.dtos.cases.UpdateCaseRequestDto
import org.company.schoolmanagementapp.application.dtos.students.AssignStudentRequestDto
import org.company.schoolmanagementapp.domain.CaseEntity
import org.company.schoolmanagementapp.domain.enums.CaseStatus
import org.company.schoolmanagementapp.infrastructure.persistence.CaseRepository
import org.company.schoolmanagementapp.infrastructure.persistence.SchoolRepository
import org.company.schoolmanagementapp.infrastructure.persistence.StudentRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class CaseService(
    val studentService: StudentService,
    val caseRepository: CaseRepository,
    val studentRepository: StudentRepository,
    val schoolRepository: SchoolRepository,
) {

    fun getCases(pageable: Pageable): PageResponse<CaseResponseDto> {
        return caseRepository.findAllWithDetails(pageable)
            .map {
                CaseResponseDto(
                    caseId = it.id,
                    studentId = it.student.id,
                    studentName = it.student.name,
                    schoolId = it.school.id,
                    schoolName = it.school.name,
                    status = it.status
                )
            }.toPageResponse()
    }

    @Transactional
    fun createCase(createCaseRequest: CreateCaseRequestDto): CaseResponseDto {
        val school = schoolRepository.findByIdOrNull(createCaseRequest.schoolId) ?: throw RuntimeException("School not found")
        val student = studentRepository.findByIdOrNull(createCaseRequest.studentId) ?: throw RuntimeException("Student not found")

        val caseEntity = caseRepository.save(
            CaseEntity(
                student = student,
                school = school,
                status = CaseStatus.PENDING
            )
        )

        return CaseResponseDto(
            caseId = caseEntity.id,
            studentId = caseEntity.student.id,
            studentName = caseEntity.student.name,
            schoolId = caseEntity.school.id,
            schoolName = caseEntity.school.name,
            status = caseEntity.status
        )
    }

    @Transactional
    fun updateCase(id: UUID, updateCaseRequest: UpdateCaseRequestDto): CaseResponseDto {
        val caseEntity = caseRepository.findByIdOrNull(id) ?: throw RuntimeException("Case not found")

        if (!caseEntity.status.getAllowedProgression().contains(updateCaseRequest.status)) {
            throw RuntimeException("Cannot update case status, invalid transition")
        }

        if (updateCaseRequest.status == CaseStatus.APPROVED) {
            studentService.assignStudentToSchool(caseEntity.student.id, AssignStudentRequestDto(caseEntity.school.id))
        }

        caseEntity.status = updateCaseRequest.status

        return CaseResponseDto(
            caseId = caseEntity.id,
            studentId = caseEntity.student.id,
            studentName = caseEntity.student.name,
            schoolId = caseEntity.school.id,
            schoolName = caseEntity.school.name,
            status = caseEntity.status
        )
    }
}
