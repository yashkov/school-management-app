package org.company.schoolmanagementapp.application.services

import org.company.schoolmanagementapp.application.dtos.*
import org.company.schoolmanagementapp.domain.SchoolEntity
import org.company.schoolmanagementapp.infrastructure.persistence.SchoolRepository
import org.company.schoolmanagementapp.infrastructure.persistence.StudentRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class SchoolService(
    val schoolRepository: SchoolRepository,
    val studentRepository: StudentRepository
) {

    fun getSchools(name: String?, pageable: Pageable): PageResponse<SchoolBasicResponseDto> {
        val schoolsPage = if (name.isNullOrBlank()) {
            schoolRepository.findAll(pageable)
        } else {
            schoolRepository.findByNameContainingIgnoreCase(name, pageable)
        }
        return schoolsPage.map { SchoolBasicResponseDto(it.id, it.name, it.capacity) }.toPageResponse()
    }

    fun getSchoolDetails(id: UUID, name: String?, pageable: Pageable): SchoolDetailsResponseDto {
        val schoolEntity = schoolRepository.findByIdOrNull(id) ?: throw RuntimeException("School not found")
        val studentsPage = if (name.isNullOrBlank()) {
            studentRepository.findBySchoolId(id, pageable)
        } else {
            studentRepository.findBySchoolIdAndNameContainingIgnoreCase(id, name, pageable)
        }

        return SchoolDetailsResponseDto(
            id = schoolEntity.id,
            name = schoolEntity.name,
            capacity = schoolEntity.capacity,
            students = studentsPage.map { StudentBasicResponseDto(it.id, it.name) }.toPageResponse()
        )
    }

    @Transactional
    fun createSchool(addSchoolRequestDto: CreateOrUpdateSchoolRequestDto): SchoolBasicResponseDto {
        val schoolEntity = schoolRepository.save(
            SchoolEntity(
                name = addSchoolRequestDto.name,
                capacity = addSchoolRequestDto.capacity
            )
        )
        return SchoolBasicResponseDto(schoolEntity.id, schoolEntity.name, schoolEntity.capacity)
    }

    @Transactional
    fun updateSchool(id: UUID, modifySchoolRequestDto: CreateOrUpdateSchoolRequestDto): SchoolBasicResponseDto {
        val schoolEntity = schoolRepository.findByIdOrNull(id) ?: throw RuntimeException("School not found")
        schoolEntity.name = modifySchoolRequestDto.name
        schoolEntity.capacity = modifySchoolRequestDto.capacity

        return SchoolBasicResponseDto(schoolEntity.id, schoolEntity.name, schoolEntity.capacity)
    }

    @Transactional
    fun deleteSchool(id: UUID) {
        schoolRepository.deleteById(id)
    }
}
