package com.github.styner9.spring.scavenger.test.fixture.context

import com.github.styner9.spring.scavenger.test.fixture.context.listener.ActiveTransactionVerifier

import com.github.styner9.spring.scavenger.test.fixture.context.listener.DependencyResolver
import com.github.styner9.spring.scavenger.test.fixture.context.listener.FixtureHolderCleaner
import com.github.styner9.spring.scavenger.test.fixture.context.listener.GenerateFixturesInvoker
import mu.KotlinLogging
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

private val logger = KotlinLogging.logger {}

class FixtureTestRunner(clazz: Class<*>): SpringJUnit4ClassRunner(clazz) {

    override fun createTestContextManager(clazz: Class<*>): TestContextManager {

        val head = listOf(
                ActiveTransactionVerifier(),
                FixtureHolderCleaner(),
                DependencyResolver())

        val tail = listOf(
                GenerateFixturesInvoker()
        )

        return super.createTestContextManager(clazz).apply {
            testExecutionListeners.apply {
                addAll(0, head)
                addAll(tail)

                logger.info {
                    "Additional TestExecutionListeners: ${head.plus(tail).map { it.javaClass.name }}"
                }
            }
        }
    }
}

