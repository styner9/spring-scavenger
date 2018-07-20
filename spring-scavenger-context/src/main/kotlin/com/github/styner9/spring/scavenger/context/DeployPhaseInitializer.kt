package com.github.styner9.spring.scavenger.context

import mu.KotlinLogging
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.Ordered
import org.springframework.core.env.Environment

private val logger = KotlinLogging.logger { }

class DeployPhaseInitializer : ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val env = applicationContext.environment

        val properties = DeployPhaseProperties(env)
        if (!properties.enabled) {
            return
        }

        val candidates = properties.candidates.filter { env.acceptsProfiles(it) }.toSet()

        val phase = when (candidates.size) {
            0 -> properties.default?.also {
                if (env.activeProfiles.isEmpty()) {
                    logger.warn("set default phase as default profile: $it")
                    env.setDefaultProfiles(*env.defaultProfiles.plus(it))
                } else {
                    logger.warn("set default phase as active profile: $it")
                    env.addActiveProfile(it)
                }
            }
            1 -> candidates.first()
            else -> {
                throw RuntimeException("ambiguous phase profiles: [${candidates.joinToString(", ")}]")
            }
        }

        if (phase == null && properties.failOnMissing) {
            throw RuntimeException()
        }

        DeployPhase.current = phase
    }

    override fun getOrder() = Ordered.HIGHEST_PRECEDENCE

    class DeployPhaseProperties(env: Environment) {

        val enabled: Boolean = env.getProperty(ENABLED, Boolean::class.java, true)

        val candidates: Set<String> = env.getProperty(CANDIDATES, "")
                .split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .toSet()

        val default: String? = env.getProperty(DEFAULT, "")
                .let {
                    if (it.isNotBlank()) it
                    else null
                }
                ?.also {
                    if (!candidates.contains(it)) throw RuntimeException()
                }

        val failOnMissing: Boolean = env.getProperty(FAIL_ON_MISSING, Boolean::class.java, false)

        companion object {
            private const val NAMESPACE = "spring-scavenger.context.deploy-phase"

            const val ENABLED = "$NAMESPACE.enabled"
            const val CANDIDATES = "$NAMESPACE.candidates"
            const val DEFAULT = "$NAMESPACE.default"
            const val FAIL_ON_MISSING = "$NAMESPACE.fail-on-missing"
        }
    }
}
