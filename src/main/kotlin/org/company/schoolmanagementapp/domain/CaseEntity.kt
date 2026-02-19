package org.company.schoolmanagementapp.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "cases")
class CaseEntity(

    @Id
    @Column(name = "case_id", nullable = false)
    var id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "student_id",
        referencedColumnName = "student_id",
        foreignKey = ForeignKey(name = "fk_case_student")
    )
    var student: StudentEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "school_id",
        referencedColumnName = "school_id",
        foreignKey = ForeignKey(name = "fk_case_school")
    )
    var school: SchoolEntity,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: CaseStatus,
)