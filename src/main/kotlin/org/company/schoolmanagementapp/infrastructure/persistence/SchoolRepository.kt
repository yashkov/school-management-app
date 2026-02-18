package org.company.schoolmanagementapp.infrastructure.persistence

import org.company.schoolmanagementapp.domain.SchoolEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SchoolRepository: JpaRepository<SchoolEntity, UUID> {

    fun findByNameContainingIgnoreCase(name: String?, pageable: Pageable): Page<SchoolEntity>
}