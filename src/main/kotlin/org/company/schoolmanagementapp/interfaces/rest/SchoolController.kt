package org.company.schoolmanagementapp.interfaces.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.company.schoolmanagementapp.application.dtos.CreateOrUpdateSchoolRequestDto
import org.company.schoolmanagementapp.application.dtos.PageResponse
import org.company.schoolmanagementapp.application.dtos.SchoolDetailsResponseDto
import org.company.schoolmanagementapp.application.dtos.SchoolBasicResponseDto
import org.company.schoolmanagementapp.application.services.SchoolService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

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
        @RequestBody createSchoolRequest: CreateOrUpdateSchoolRequestDto
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
        @RequestBody updateSchoolRequest: CreateOrUpdateSchoolRequestDto
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
