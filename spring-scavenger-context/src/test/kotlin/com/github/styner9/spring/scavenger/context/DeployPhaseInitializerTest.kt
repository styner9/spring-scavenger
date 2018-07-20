package com.github.styner9.spring.scavenger.context

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.message
import org.junit.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import java.util.*

class DeployPhaseInitializerTest : AbstractDeployPhaseResettingTest() {

    @SpringBootApplication
    open class App

    private val defaultProperties = Properties().apply {
        setProperty(DeployPhaseInitializer.DeployPhaseProperties.CANDIDATES, "dev,test,prod")
        setProperty(DeployPhaseInitializer.DeployPhaseProperties.DEFAULT, "dev")
        setProperty(DeployPhaseInitializer.DeployPhaseProperties.FAIL_ON_MISSING, "true")
    }

    private fun runApplication(profiles: Array<String> = emptyArray(), properties: Map<String, Any> = emptyMap(), withoutInitializer: Boolean = false) {
        SpringApplicationBuilder()
                .profiles(*profiles)
                .properties(properties)
                .properties(defaultProperties)
                .sources(App::class.java)
                .build()
                .apply {
                    if (withoutInitializer) setInitializers(emptyList())
                }
                .run()
    }

    private fun assertCurrentPhase(expected: String) {
        assert(DeployPhase.current).isEqualTo(expected)
    }

    @Test
    fun withDefault() {
        runApplication()
        assertCurrentPhase("dev")
    }

    @Test
    fun whenActiveProfileSpecified() {
        runApplication(profiles = arrayOf("test"))
        assertCurrentPhase("test")
    }

    @Test
    fun whenDefaultProfileActivated() {
        runApplication(properties = mapOf("spring.profiles.default" to "test"))
        assertCurrentPhase("test")
    }

    @Test
    fun whenDefaultProfileIgnored() {
        runApplication(
                profiles = arrayOf("hello"),
                properties = mapOf("spring.profiles.default" to "test"))
        assertCurrentPhase("dev")
    }

    @Test
    fun withoutInitializer() {
        runApplication(withoutInitializer = true)
        assert {
            DeployPhase.current
        }.thrownError {
            message().isEqualTo("not initialized")
            isInstanceOf(IllegalStateException::class.java)
        }
    }

    @Test
    fun withAmbiguousActiveProfiles() {
        assert {
            runApplication(arrayOf("dev", "test"))
        }.thrownError {
            message().isEqualTo("ambiguous phase profiles: [dev, test]")
            isInstanceOf(RuntimeException::class.java)
        }
    }
}
