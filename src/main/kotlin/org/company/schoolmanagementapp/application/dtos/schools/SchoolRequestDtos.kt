package org.company.schoolmanagementapp.application.dtos.schools

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class CreateOrUpdateSchoolRequestDto(
    val name: String,
    @field:Min(50, message = "School capacity must be at least 50")
    @field:Max(2000, message = "School capacity must be at most 2000")
    val capacity: Int
)
