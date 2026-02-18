package org.company.schoolmanagementapp.application.dtos

data class CreateOrUpdateSchoolRequestDto(
    val name: String,
    val capacity: Int
)