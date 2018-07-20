package com.github.styner9.spring.scavenger.test.demo.repository

import assertk.assert
import assertk.assertions.*
import com.github.styner9.spring.scavenger.test.fixture.SimpleFixtureTest
import com.github.styner9.spring.scavenger.test.fixture.core.FixtureGenerator
import com.github.styner9.spring.scavenger.test.fixture.core.Spec
import com.github.styner9.spring.scavenger.test.fixture.core.annotation.Fixture
import com.github.styner9.spring.scavenger.test.fixture.core.annotation.FixtureSpec
import com.github.styner9.spring.scavenger.test.demo.model.Customer
import com.github.styner9.spring.scavenger.test.demo.model.ZipCode
import com.github.styner9.spring.scavenger.test.demo.repository.annotation.DemoFixtureTest
import com.github.styner9.spring.scavenger.test.demo.repository.fixture.ZipCodeFixtureGenerator
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.dao.DataIntegrityViolationException
import kotlin.test.assertFailsWith

@DemoFixtureTest
open class CustomerRepositoryTest: SimpleFixtureTest<CustomerRepository, Customer>() {

    @Autowired
    private lateinit var zipCodeGenerator: ZipCodeFixtureGenerator

    @Fixture
    private lateinit var zipCode: ZipCode

    @Autowired
    private lateinit var entityManager: TestEntityManager

    override fun getFixtureGenerator(): FixtureGenerator<Customer> {
        return object : FixtureGenerator<Customer>() {
            override fun initialize(spec: Spec) {
                if (!::zipCode.isInitialized) {
                    zipCode = zipCodeGenerator.generateFixtures(1) { i, obj ->
                        obj.copy(code = "zip_code_#${i}_for_customer")
                    }.first()
                }
            }

            override fun instantiate(spec: Spec, i: Int): Customer {
                return Customer(firstName = "first_$i", lastName = "last_$i", zipCode = zipCode)
            }

            override fun save(obj: Customer) {
                repository.save(obj)
            }
        }
    }

    @Test
    @FixtureSpec(10)
    fun testFixturesWithFixtureSpec() {
        assert(fixtures).isNotNull()
        assert(fixtures.size).isEqualTo(10)
        fixtures.forEach {
            assert(it.id).isGreaterThan(0L)
            assert(it.firstName).isNotEmpty()
            assert(it.lastName).isNotEmpty()
            assert(it.zipCode).isEqualTo(zipCode)
        }
    }

    @Test
    fun testFixturesWithoutFixtureSpec() {
        assertFailsWith(UninitializedPropertyAccessException::class) { fixtures }
        assertFailsWith(UninitializedPropertyAccessException::class) { fixture }
        assertFailsWith(UninitializedPropertyAccessException::class) { zipCode }
    }

    @Test
    @FixtureSpec
    fun testSelect() {
        assert(repository.findById(fixture.id).get()).isEqualTo(fixture)
        assert(repository.findByLastName(fixture.lastName)).containsExactly(fixture)
        assert(repository.findByLastName("unknown")).isEmpty()
        assert(repository.findAll()).isEqualTo(fixtures)
    }

    @Test
    fun testInsert() {
        assert(repository.findAll().toList()).isEmpty()

        getFixtureGenerator().run {
            listOf(
                    newInstances().first().copy(firstName = "12345678901"),
                    newInstances().first().copy(lastName = "12345678901")
            )
        }.forEach {
            assertFailsWith(DataIntegrityViolationException::class) {
                try {
                    repository.saveAndFlush(it)
                } finally {
                    entityManager.clear()
                }
            }
        }

        val obj1 = repository.saveAndFlush(Customer("hello", "world")).apply {
            assert(id).isGreaterThan(0L)
            assert(firstName).isEqualTo("hello")
            assert(lastName).isEqualTo("world")
            assert(zipCode).isNull()
        }

        assertFailsWith(DataIntegrityViolationException::class) {
            try {
                repository.saveAndFlush(obj1.copy(id = 0))
            } finally {
                entityManager.clear()
            }
        }

        val obj2 = repository.saveAndFlush(Customer(0, "karl", "styner", zipCodeGenerator.generateFixtures().first())).apply {
            assert(id).isGreaterThan(0L)
            assert(firstName).isEqualTo("karl")
            assert(lastName).isEqualTo("styner")
            assert(zipCode).isNotNull()
        }

        assert(obj1.id).isLessThan(obj2.id)

        assert(repository.findAll()).isEqualTo(listOf(obj1, obj2))
    }

    @Test
    @FixtureSpec
    fun testUpdate() {
        assertFailsWith(DataIntegrityViolationException::class) {
            try {
                repository.saveAndFlush(fixture.copy(firstName = "123123123123123"))
            } finally {
                entityManager.clear()
            }
        }

        repository.saveAndFlush(fixture.copy(firstName = "hello")).apply {
            assert(firstName).isEqualTo("hello")
        }
    }
}

