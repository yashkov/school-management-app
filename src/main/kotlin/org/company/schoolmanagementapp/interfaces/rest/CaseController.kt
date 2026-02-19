package org.company.schoolmanagementapp.interfaces.rest

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.company.schoolmanagementapp.application.dtos.cases.CaseResponseDto
import org.company.schoolmanagementapp.application.dtos.cases.CreateCaseRequestDto
import org.company.schoolmanagementapp.application.dtos.PageResponse
import org.company.schoolmanagementapp.application.dtos.cases.UpdateCaseRequestDto
import org.company.schoolmanagementapp.application.services.CaseService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/cases")
class CaseController(
    val caseService: CaseService
) {

    @Operation(
        summary = "Get cases",
        description = "Endpoint for retrieving list of all cases."
    )
    @GetMapping
    fun getCases(pageable: Pageable): PageResponse<CaseResponseDto> = caseService.getCases(pageable)

    @Operation(
        summary = "Create case",
        description = "Endpoint for creating a new case."
    )
    @PostMapping
    fun createCase(
        @Valid @RequestBody createCaseRequest: CreateCaseRequestDto
    ): ResponseEntity<CaseResponseDto> {
        val created = caseService.createCase(createCaseRequest)
        return ResponseEntity
            .created(URI.create("/cases/${created.caseId}"))
            .body(created)
    }

    @Operation(
        summary = "Update case",
        description = "Endpoint for updating case status."
    )
    @PutMapping("/{id}")
    fun updateCase(
        @PathVariable id: UUID,
        @RequestBody updateCaseRequest: UpdateCaseRequestDto
    ): ResponseEntity<CaseResponseDto> {
        val updated = caseService.updateCase(id, updateCaseRequest)
        return ResponseEntity.ok(updated)
    }
}
