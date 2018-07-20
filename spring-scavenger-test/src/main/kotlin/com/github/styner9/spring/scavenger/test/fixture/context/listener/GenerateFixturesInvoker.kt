package com.github.styner9.spring.scavenger.test.fixture.context.listener

import com.github.styner9.spring.scavenger.test.fixture.FixtureTest
import com.github.styner9.spring.scavenger.test.fixture.core.Spec
import com.github.styner9.spring.scavenger.test.fixture.core.annotation.FixtureSpec
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import java.lang.reflect.AnnotatedElement

internal class GenerateFixturesInvoker: AbstractTestExecutionListener() {

    override fun beforeTestMethod(testContext: TestContext) {
        val testInstance = testContext.testInstance
        if (testInstance is FixtureTest<*>) {
            val ann = FixtureSpec::class.java
            (AnnotationUtils.findAnnotation(testContext.testMethod as AnnotatedElement, ann)
                    ?: AnnotationUtils.findAnnotation(testContext.testClass, ann))
                    ?.takeIf { it.size > 0 }
                    ?.apply { testInstance.generateFixtures(Spec(size, profiles.toSet())) }
        }
    }
}