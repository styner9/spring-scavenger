package com.github.styner9.spring.scavenger.test.demo.repository

import com.github.styner9.spring.scavenger.test.demo.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: JpaRepository<Customer, Long> {
    fun findByLastName(lastName: String): List<Customer>
}