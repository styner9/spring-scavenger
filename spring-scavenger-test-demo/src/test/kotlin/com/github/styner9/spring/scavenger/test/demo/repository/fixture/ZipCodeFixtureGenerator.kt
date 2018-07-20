package com.github.styner9.spring.scavenger.test.demo.repository.fixture

import com.github.styner9.spring.scavenger.test.fixture.core.FixtureGenerator
import com.github.styner9.spring.scavenger.test.fixture.core.Spec
import com.github.styner9.spring.scavenger.test.demo.model.ZipCode
import com.github.styner9.spring.scavenger.test.demo.repository.ZipCodeRepository
import org.springframework.stereotype.Component

@Component
open class ZipCodeFixtureGenerator(private val repository: ZipCodeRepository): FixtureGenerator<ZipCode>() {

    override fun instantiate(spec: Spec, i: Int): ZipCode {
        return ZipCode(code = "zip_$i")
    }

    override fun save(obj: ZipCode) {
        repository.save(obj)
    }
}