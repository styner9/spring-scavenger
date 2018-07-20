package com.github.styner9.spring.scavenger.context.demo

import com.github.styner9.spring.scavenger.context.DeployPhase
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import java.util.*

private val logger = KotlinLogging.logger { }

@SpringBootApplication
open class App {

    @Bean
    open fun props() = Properties().also {
        logger.info("hello ${DeployPhase.current}")
    }

    @Bean
    @Profile("dev")
    open fun devProps() = Properties().also {
        logger.info("hello in dev")
    }

    @Bean
    @Profile("prod")
    open fun prodProps() = Properties().also {
        logger.info("hello in prod")
    }
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}