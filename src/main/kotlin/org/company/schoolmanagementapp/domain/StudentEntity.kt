package org.company.schoolmanagementapp.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "students")
class StudentEntity(

    @Id
    @Column(name = "student_id", nullable = false, unique = true)
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var name: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
        name = "school_id",
        referencedColumnName = "school_id",
        foreignKey = ForeignKey(name = "fk_student_school")
    )
    var school: SchoolEntity? = null
)
