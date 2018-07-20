package com.github.styner9.spring.scavenger.context

import org.junit.Test
import assertk.*
import assertk.assertions.*

class DeployPhaseTest : AbstractDeployPhaseResettingTest() {

    @Test
    fun testCurrent() {
        assert {
            DeployPhase.current
        }.thrownError {
            isInstanceOf(IllegalStateException::class)
            message().isEqualTo("not initialized")
        }

        // null <- null: OK
        DeployPhase.current = null

        // null <- dev : OK
        DeployPhase.current = "dev"

        // dev <- dev : OK
        DeployPhase.current = "dev"

        // dev <- test : Error
        assert {
            DeployPhase.current = "test"
        }.thrownError {
            isInstanceOf(IllegalStateException::class)
            message().isEqualTo("initialized already")
        }
    }

    @Test
    fun testActive() {
        assert {
            DeployPhase.active("dev")
        }.thrownError {
            isInstanceOf(IllegalStateException::class)
            message().isEqualTo("not initialized")
        }

        DeployPhase.current = "dev"

        assert(DeployPhase.active("dev")).isTrue()
        assert(DeployPhase.active("DEV")).isFalse()
    }
}