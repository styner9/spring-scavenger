package com.github.styner9.spring.scavenger.test.demo.repository

import assertk.assert
import assertk.assertions.isEqualTo
import com.github.styner9.spring.scavenger.test.fixture.SimpleFixtureTest
import com.github.styner9.spring.scavenger.test.fixture.core.annotation.FixtureSpec
import com.github.styner9.spring.scavenger.test.demo.model.ZipCode
import com.github.styner9.spring.scavenger.test.demo.repository.annotation.DemoFixtureTest
import com.github.styner9.spring.scavenger.test.demo.repository.fixture.ZipCodeFixtureGenerator
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

@DemoFixtureTest
open class ZipCodeRepositoryTest: SimpleFixtureTest<ZipCodeRepository, ZipCode>() {

    @Autowired
    private lateinit var fixtureGenerator: ZipCodeFixtureGenerator

    @Test
    @FixtureSpec
    fun testFindByCode() {
        assert(repository.findByCode(fixture.code)).isEqualTo(fixture)
    }

}