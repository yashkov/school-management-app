package org.company.schoolmanagementapp.interfaces.rest

import io.swagger.v3.oas.annotations.Operation
import org.company.schoolmanagementapp.application.dtos.*
import org.company.schoolmanagementapp.application.dtos.students.AssignStudentRequestDto
import org.company.schoolmanagementapp.application.dtos.students.CreateOrUpdateStudentRequestDto
import org.company.schoolmanagementapp.application.dtos.students.StudentBasicResponseDto
import org.company.schoolmanagementapp.application.dtos.students.StudentDetailsResponseDto
import org.company.schoolmanagementapp.application.services.StudentService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/students")
class StudentController(
    val studentService: StudentService
) {

    @Operation(
        summary = "Get students",
        description = "Endpoint for retrieving list of all students. Can be filtered by optional student name."
    )
    @GetMapping
    fun getStudents(
        @RequestParam("name", required = false) name: String?,
        pageable: Pageable
    ): PageResponse<StudentBasicResponseDto> = studentService.getStudents(name, pageable)

    @Operation(
        summary = "Get student details",
        description = "Endpoint for retrieving details about specific student."
    )
    @GetMapping("/{id}")
    fun getStudentDetails(@PathVariable id: UUID): StudentDetailsResponseDto =
        studentService.getStudentDetails(id)

    @Operation(
        summary = "Create student",
        description = "Endpoint for creating a new student."
    )
    @PostMapping
    fun addStudent(
        @RequestBody createStudentRequest: CreateOrUpdateStudentRequestDto
    ): ResponseEntity<StudentBasicResponseDto> {
        val created = studentService.createStudent(createStudentRequest)
        return ResponseEntity
            .created(URI.create("/students/${created.id}"))
            .body(created)
    }

    @Operation(
        summary = "Update student",
        description = "Endpoint for updating existing student data."
    )
    @PutMapping("/{id}")
    fun updateStudent(
        @PathVariable id: UUID,
        @RequestBody updateStudentRequest: CreateOrUpdateStudentRequestDto
    ): ResponseEntity<StudentDetailsResponseDto> {
        val updated = studentService.updateStudent(id, updateStudentRequest)
        return ResponseEntity.ok(updated)
    }

    @Operation(
        summary = "Assign student to school",
        description = "Endpoint for assigning student to school or clearing assignment."
    )
    @PutMapping("/{id}/assign")
    fun assignStudentToSchool(
        @PathVariable id: UUID,
        @RequestBody assignStudentRequest: AssignStudentRequestDto
    ): ResponseEntity<StudentDetailsResponseDto> {
        val assigned = studentService.assignStudentToSchool(id, assignStudentRequest)
        return ResponseEntity.ok(assigned)
    }

    @Operation(
        summary = "Delete student",
        description = "Endpoint for deleting student by id."
    )
    @DeleteMapping("/{id}")
    fun deleteStudent(
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        studentService.deleteStudent(id)
        return ResponseEntity.noContent().build()
    }
}
