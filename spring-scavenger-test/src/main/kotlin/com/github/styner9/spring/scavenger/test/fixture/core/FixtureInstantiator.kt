package com.github.styner9.spring.scavenger.test.fixture.core

import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

abstract class FixtureInstantiator<T: Any> {

    open fun newInstances(spec: Spec): List<T> {
        return (0 until spec.size).map { instantiate(spec, it) }.also {
            logger.debug {
                "instantiated: type=${it.first().javaClass.name}, size=${spec.size}, profiles=${spec.profiles}"
            }
        }
    }

    @JvmOverloads
    open fun newInstances(size: Int = 1) = newInstances(Spec(size))

    protected abstract fun instantiate(spec: Spec, i: Int): T

}

