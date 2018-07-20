package com.github.styner9.spring.scavenger.context

import org.junit.Before

abstract class AbstractDeployPhaseResettingTest {

    @Before
    fun setUp() {
        try {
            DeployPhase.reset()
        } catch (_: Exception) {

        }
    }
}