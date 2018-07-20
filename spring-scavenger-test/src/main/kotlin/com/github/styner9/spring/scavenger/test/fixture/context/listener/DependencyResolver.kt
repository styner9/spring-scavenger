package com.github.styner9.spring.scavenger.test.fixture.context.listener

import com.github.styner9.spring.scavenger.test.fixture.FixtureTest
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ResolvableType
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.springframework.util.ReflectionUtils

private val logger = KotlinLogging.logger {  }

internal class DependencyResolver : AbstractTestExecutionListener() {

    override fun prepareTestInstance(testContext: TestContext) {
        registry = Registry(testContext)
    }

    companion object {
        private lateinit var registry: Registry

        @JvmStatic
        internal fun contains(clazz: Class<*>): Boolean {
            return registry.contains(clazz)
        }
    }

    private class Registry internal constructor(context: TestContext) {
        private val types: Set<ResolvableType> by lazy {
            val testClass = context.testClass

            if (!FixtureTest::class.java.isAssignableFrom(testClass)) {
                throw IllegalArgumentException()
            }

            val types: MutableSet<ResolvableType> = mutableSetOf(
                    ResolvableType.forClass(testClass),
                    ResolvableType.forClass(getGenericTypeOf(testClass)!!))

            types.flatMap { scan(types, it) }.apply {
                types.addAll(this)
            }

            logger.info {
                "Found ${types.size} types: $types"
            }

            types
        }

        private fun getGenericTypeOf(clazz: Class<*>): Class<*>? {
            return ResolvableType.forClass(clazz).superType.getGeneric(0).resolve()
        }

        private fun scan(found: Set<ResolvableType>, containerType: ResolvableType): Set<ResolvableType> {
            val set = mutableSetOf<ResolvableType>()

            containerType.resolve()?.run {
                ReflectionUtils.doWithFields(this) {
                    if (AnnotationUtils.findAnnotation(it, Autowired::class.java) != null) {
                        ResolvableType.forField(it)
                                .takeIf { it !in found && it.resolve()?.let { it != Any::class.java } ?: false }
                                ?.apply { set.add(this) }
                    }
                }
            }

            set.flatMap { t -> scan(found.union(set), t) }.apply {
                set.addAll(this)
            }

            return set
        }

        fun contains(candidate: Class<*>): Boolean {
            return types.any {
                it.resolve()?.let {
                    it.isInterface && it == getGenericTypeOf(candidate) || it.isAssignableFrom(candidate)
                } ?: false
            }
        }
    }
}