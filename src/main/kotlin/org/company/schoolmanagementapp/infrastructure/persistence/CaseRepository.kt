package org.company.schoolmanagementapp.infrastructure.persistence

import org.company.schoolmanagementapp.domain.CaseEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CaseRepository: JpaRepository<CaseEntity, UUID> {

    @Query("""
        SELECT c FROM CaseEntity c
        JOIN FETCH c.student
        JOIN FETCH c.school
    """)
    fun findAllWithDetails(pageable: Pageable): Page<CaseEntity>
}