package org.company.schoolmanagementapp.interfaces.rest

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.company.schoolmanagementapp.application.dtos.schools.CreateOrUpdateSchoolRequestDto
import org.company.schoolmanagementapp.application.dtos.PageResponse
import org.company.schoolmanagementapp.application.dtos.schools.SchoolBasicResponseDto
import org.company.schoolmanagementapp.application.dtos.schools.SchoolDetailsResponseDto
import org.company.schoolmanagementapp.application.services.SchoolService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/schools")
class SchoolController(
    val schoolService: SchoolService
) {

    @Operation(
        summary = "Get schools",
        description = "Endpoint for retrieving list of all schools. Can be filtered by optional school name."
    )
    @GetMapping
    fun getSchools(
        @RequestParam("name", required = false) name: String?,
        pageable: Pageable
    ): PageResponse<SchoolBasicResponseDto> = schoolService.getSchools(name, pageable)

    @Operation(
        summary = "Get school details",
        description = "Endpoint for retrieving details about specific school with nested list of all assigned students. " +
                "Students' list can be filtered by optional student name."
    )
    @GetMapping("/{id}")
    fun getSchoolDetails(
        @PathVariable id: UUID,
        @RequestParam("name", required = false) name: String?,
        pageable: Pageable
    ): SchoolDetailsResponseDto = schoolService.getSchoolDetails(id, name, pageable)

    @Operation(
        summary = "Create school",
        description = "Endpoint for creating a new school."
    )
    @PostMapping
    fun createSchool(
        @Valid @RequestBody createSchoolRequest: CreateOrUpdateSchoolRequestDto
    ): ResponseEntity<SchoolBasicResponseDto> {
        val created = schoolService.createSchool(createSchoolRequest)
        return ResponseEntity
            .created(URI.create("/schools/${created.id}"))
            .body(created)
    }

    @Operation(
        summary = "Update school",
        description = "Endpoint for updating existing school data."
    )
    @PutMapping("/{id}")
    fun updateSchool(
        @PathVariable id: UUID,
        @Valid @RequestBody updateSchoolRequest: CreateOrUpdateSchoolRequestDto
    ): ResponseEntity<SchoolBasicResponseDto> {
        val updated = schoolService.updateSchool(id, updateSchoolRequest)
        return ResponseEntity.ok(updated)
    }

    @Operation(
        summary = "Delete school",
        description = "Endpoint for deleting school by id."
    )
    @DeleteMapping("/{id}")
    fun deleteSchool(@PathVariable id: UUID): ResponseEntity<Void> {
        schoolService.deleteSchool(id)
        return ResponseEntity.noContent().build()
    }
}
