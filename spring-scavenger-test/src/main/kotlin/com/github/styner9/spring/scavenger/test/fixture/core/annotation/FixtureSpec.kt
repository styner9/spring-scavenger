package com.github.styner9.spring.scavenger.test.fixture.core.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class FixtureSpec(
        val size: Int = 1,
        val profiles: Array<String> = []
)