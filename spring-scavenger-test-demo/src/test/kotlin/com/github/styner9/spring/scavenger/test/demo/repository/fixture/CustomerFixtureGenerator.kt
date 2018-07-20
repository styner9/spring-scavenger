package com.github.styner9.spring.scavenger.test.demo.repository.fixture

import com.github.styner9.spring.scavenger.test.fixture.core.FixtureGenerator
import com.github.styner9.spring.scavenger.test.fixture.core.Spec
import com.github.styner9.spring.scavenger.test.fixture.core.annotation.Fixture
import com.github.styner9.spring.scavenger.test.demo.model.Customer
import com.github.styner9.spring.scavenger.test.demo.model.ZipCode
import com.github.styner9.spring.scavenger.test.demo.repository.CustomerRepository
import org.springframework.stereotype.Component

@Component
class CustomerFixtureGenerator(
        private val repository: CustomerRepository,
        private val zipCodeFixtureGenerator: ZipCodeFixtureGenerator
): FixtureGenerator<Customer>() {

    @Fixture
    private var zipCode: ZipCode? = null

    override fun initialize(spec: Spec) {
        zipCode = zipCodeFixtureGenerator.generateFixtures(1) { i, obj ->
            obj.copy(code = "zip_code_#${i}_for_customer")
        }.first()
    }

    override fun instantiate(spec: Spec, i: Int): Customer {
        return Customer(firstName = "first_$i", lastName = "last_$i", zipCode = zipCode)
    }

    override fun save(obj: Customer) {
        repository.save(obj)
    }

}