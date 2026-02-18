package org.company.schoolmanagementapp.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import java.util.UUID

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
