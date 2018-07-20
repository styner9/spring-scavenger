package com.github.styner9.spring.scavenger.test.demo.model

import javax.persistence.*

@Entity
data class ZipCode(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false, unique = true)
        val code: String = ""
)