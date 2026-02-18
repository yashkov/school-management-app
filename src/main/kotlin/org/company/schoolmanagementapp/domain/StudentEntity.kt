package org.company.schoolmanagementapp.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "students")
class StudentEntity(

    @Id
    @Column(name = "student_id", nullable = false)
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
