package com.github.styner9.spring.scavenger.test.demo.repository

import com.github.styner9.spring.scavenger.test.demo.model.ZipCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ZipCodeRepository: JpaRepository<ZipCode, Long> {
    fun findByCode(code: String): ZipCode
}