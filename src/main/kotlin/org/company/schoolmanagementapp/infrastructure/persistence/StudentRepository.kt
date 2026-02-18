package org.company.schoolmanagementapp.infrastructure.persistence

import org.company.schoolmanagementapp.domain.StudentEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StudentRepository: JpaRepository<StudentEntity, UUID> {

    fun findBySchoolId(schoolId: UUID, pageable: Pageable): Page<StudentEntity>

    fun findBySchoolIdAndNameContainingIgnoreCase(schoolId: UUID, name: String?, pageable: Pageable): Page<StudentEntity>

    fun findByNameContainingIgnoreCase(name: String?, pageable: Pageable): Page<StudentEntity>
}
