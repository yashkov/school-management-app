package org.company.schoolmanagementapp.interfaces.rest

import org.company.schoolmanagementapp.application.exceptions.BusinessException
import org.company.schoolmanagementapp.application.exceptions.DataNotFoundException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    // Handling of some exemplary exceptions - most probably should be expanded

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(ex: MethodArgumentNotValidException): ProblemDetail {
        val problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY)

        problem.title = "Request cannot be processed due to validation errors"

        val errors = ex.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "invalid")
        }
        problem.setProperty("errors", errors)

        return problem
    }

    @ExceptionHandler(BusinessException::class)
    fun handle(ex: BusinessException): ProblemDetail {
        val problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY)

        problem.title = "Request cannot be processed"
        problem.detail = ex.message

        return problem
    }

    @ExceptionHandler(DataNotFoundException::class)
    fun handle(ex: DataNotFoundException): ProblemDetail {
        val problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND)

        problem.title = "Data not found"
        problem.detail = ex.message

        return problem
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handle(ex: DataIntegrityViolationException): ProblemDetail {
        val rootMessage = ex.rootCause?.message.orEmpty()
        return if (
            rootMessage.contains("uq_school_name_ci", ignoreCase = true) ||
            rootMessage.contains("schools_name_key", ignoreCase = true)
        ) {
            val problem = ProblemDetail.forStatus(HttpStatus.CONFLICT)

            problem.title = "Could not persist data"
            problem.detail = "School with requested name already exists"
            problem
        } else if (rootMessage.contains("chk_school_capacity", ignoreCase = true)) {
            val problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY)

            // Potentially some additional logging as this should have been handled by Spring validation

            problem.title = "Could not persist data"
            problem.detail = "School capacity out of allowed range (50 - 2000)"
            problem
        } else {
            val problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)

            // Potentially log details for analysis - requests breaking data integrity should be preferably caught before reaching this stage

            problem.title = "Could not persist data"
            problem
        }
    }

    @ExceptionHandler(RuntimeException::class)
    fun handle(ex: RuntimeException): ProblemDetail {
        val problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)

        // Potentially log details for analysis

        problem.title = "Critical error occurred"
        problem.detail = "An unexpected error occurred. Please try again later."

        return problem
    }

}