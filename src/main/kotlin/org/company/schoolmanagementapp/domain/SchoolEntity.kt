package org.company.schoolmanagementapp.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "schools")
class SchoolEntity(

    @Id
    @Column(name = "school_id", nullable = false, unique = true)
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var capacity: Int
) {

    @OneToMany(
        mappedBy = "school",
        fetch = FetchType.LAZY
    )
    @OrderBy("name ASC")
    val students: MutableSet<StudentEntity> = mutableSetOf()
}
