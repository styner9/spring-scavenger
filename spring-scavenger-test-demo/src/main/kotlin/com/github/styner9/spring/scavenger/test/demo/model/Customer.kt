package com.github.styner9.spring.scavenger.test.demo.model

import javax.persistence.*

@Entity
@Table(uniqueConstraints = [
    (UniqueConstraint(columnNames = ["firstName", "lastName"]))
])
data class Customer(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false, length = 10)
        val firstName: String = "",

        @Column(nullable = false, length = 10)
        val lastName: String = "",

        @ManyToOne
        @PrimaryKeyJoinColumn
        val zipCode: ZipCode? = null
) {
    constructor(firstName: String, lastName: String): this(firstName = firstName, lastName = lastName, zipCode = null)
}