package com.github.styner9.spring.scavenger.test.demo.repository.annotation

import com.github.styner9.spring.scavenger.test.fixture.context.annotation.SpringBootFixtureTest
import com.github.styner9.spring.scavenger.test.demo.Application
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootFixtureTest(
        contextConfigurationClasses = [ Application::class ],
        componentScanBasePackages = [ "com.github.styner9.spring.scavenger.test.demo" ]
)
@DataJpaTest
@Inherited
annotation class DemoFixtureTest