package com.github.styner9.spring.scavenger.test.fixture.context.listener

import com.github.styner9.spring.scavenger.test.fixture.core.FixtureHolder
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

internal class FixtureHolderCleaner: AbstractTestExecutionListener() {

    override fun beforeTestMethod(testContext: TestContext) {
        testContext.apply {
            applicationContext
                    .getBeansOfType(FixtureHolder::class.java)
                    .values
                    .toMutableList()
                    .apply {
                        if (testInstance is FixtureHolder) {
                            add(testInstance as FixtureHolder)
                        }
                    }
                    .forEach { it.clearFixtures() }
        }
    }
}