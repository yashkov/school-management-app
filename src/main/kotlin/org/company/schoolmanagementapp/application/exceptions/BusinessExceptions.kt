package org.company.schoolmanagementapp.application.exceptions

abstract class BusinessException : RuntimeException {
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
}

class SchoolCapacityExceededException : BusinessException("Cannot be assigned to school, school is at full capacity")

class InvalidCaseStatusTransitionException : BusinessException("Cannot update case status, invalid transition")