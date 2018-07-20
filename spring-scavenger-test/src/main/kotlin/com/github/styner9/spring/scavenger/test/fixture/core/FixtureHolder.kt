package com.github.styner9.spring.scavenger.test.fixture.core

import com.github.styner9.spring.scavenger.test.fixture.core.annotation.Fixture
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

interface FixtureHolder {
    @JvmDefault
    fun clearFixtures() {
        javaClass.declaredFields?.filter { it.isAnnotationPresent(Fixture::class.java) }?.forEach {
            if (!it.isAccessible) {
                it.isAccessible = true
            }

            it.set(this, null)

            logger.debug {
                "cleared: ${it.declaringClass.name}#${it.name}"
            }
        }
    }
}