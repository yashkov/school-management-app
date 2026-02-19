package org.company.schoolmanagementapp.application.exceptions

abstract class TechnicalException : RuntimeException {
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
}

abstract class DataNotFoundException(message: String) : TechnicalException(message)

class SchoolNotFoundException : DataNotFoundException("School not found")

class StudentNotFoundException : DataNotFoundException("Student not found")

class CaseNotFoundException : DataNotFoundException("Case not found")