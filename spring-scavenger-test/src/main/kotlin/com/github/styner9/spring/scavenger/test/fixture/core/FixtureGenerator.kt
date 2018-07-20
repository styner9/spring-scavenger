package com.github.styner9.spring.scavenger.test.fixture.core

import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

abstract class FixtureGenerator<T: Any>: FixtureInstantiator<T>(), FixtureHolder {

    override fun newInstances(spec: Spec): List<T> {
        initialize(spec)
        return super.newInstances(spec)
    }

    override fun newInstances(size: Int): List<T> {
        initialize(Spec(size))
        return super.newInstances(size)
    }

    @JvmOverloads
    fun generateFixtures(spec: Spec, editor: ((Int, T) -> T)? = null): List<T> {
        return newInstances(spec).mapIndexed { i, instance ->
            editor?.invoke(i, instance) ?: instance
        }.also { list ->
            list.forEach { save(it) }

            logger.debug {
                "generated: type=${list.first().javaClass.name}, size=${list.size}, profiles=${spec.profiles}"
            }
        }
    }

    @JvmOverloads
    fun generateFixtures(size: Int = 1, editor: ((Int, T) -> T)? = null): List<T> {
        return generateFixtures(Spec(size), editor)
    }

    protected open fun initialize(spec: Spec) = Unit

    protected abstract fun save(obj: T)
}