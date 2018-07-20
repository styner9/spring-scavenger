package com.github.styner9.spring.scavenger.test.fixture

import com.github.styner9.spring.scavenger.test.fixture.core.FixtureGenerator
import com.github.styner9.spring.scavenger.test.fixture.core.FixtureHolder
import com.github.styner9.spring.scavenger.test.fixture.core.Spec
import com.github.styner9.spring.scavenger.test.fixture.core.annotation.Fixture
import com.github.styner9.spring.scavenger.test.support.ReflectionUtil
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.util.ReflectionTestUtils

private val logger = KotlinLogging.logger { }

@Suppress("UNCHECKED_CAST")
abstract class SimpleFixtureTest<R: Any, E: Any>: FixtureTest<R>(), FixtureHolder {

    @Fixture
    protected lateinit var fixtures: List<E>

    @Fixture
    protected lateinit var fixture: E

    private lateinit var fixtureGeneratorCache: FixtureGenerator<E>

    protected open fun getFixtureGenerator(): FixtureGenerator<E> {
        if (!::fixtureGeneratorCache.isInitialized) {
            val modelClazz = ReflectionUtil.getTypeArgumentOfGenericSuperclass(javaClass, 1)
            val fields = ReflectionUtil.getDeclaredFields(javaClass) { field ->
                FixtureGenerator::class.java.isAssignableFrom(field.type)
                        && field.getAnnotation(Autowired::class.java) != null
                        && modelClazz == ReflectionUtil.getTypeArgumentOfGenericSuperclass(field.type, 0)
            }

            if (fields.size == 1) {
                fixtureGeneratorCache = ReflectionTestUtils.getField(this, javaClass, fields.first().name).let {
                    it as FixtureGenerator<E>
                }.also {
                    logger.debug {
                        "using fixture generator: $it"
                    }
                }
            } else {
                throw IllegalStateException("can't find fixture generator for $modelClazz. you should override 'getFixtureGenerator' method properly.")
            }
        }
        return fixtureGeneratorCache
    }

    override fun generateFixtures(spec: Spec) {
        getFixtureGenerator().generateFixtures(spec).also {
            fixtures = it
            fixture = it.first()
        }
    }

    protected fun newInstances(spec: Spec): List<E> = getFixtureGenerator().newInstances(spec)

    protected fun newInstance(): E = newInstances(Spec(1)).first()

}