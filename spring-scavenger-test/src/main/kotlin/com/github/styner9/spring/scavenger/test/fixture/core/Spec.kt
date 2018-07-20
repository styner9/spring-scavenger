package com.github.styner9.spring.scavenger.test.fixture.core

data class Spec @JvmOverloads constructor(
        val size: Int,
        val profiles: Set<String> = emptySet()
)