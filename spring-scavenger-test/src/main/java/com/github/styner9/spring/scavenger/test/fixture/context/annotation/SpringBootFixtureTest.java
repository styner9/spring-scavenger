package com.github.styner9.spring.scavenger.test.fixture.context.annotation;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@SpringFixtureTest
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Inherited
public @interface SpringBootFixtureTest {

    @AliasFor(annotation = SpringFixtureTest.class, attribute = "contextConfigurationClasses")
    Class[] contextConfigurationClasses() default {};

    @AliasFor(annotation = SpringFixtureTest.class, attribute = "contextInitializers")
    Class<? extends ApplicationContextInitializer<?>>[] contextInitializers() default {};

    @AliasFor(annotation = SpringFixtureTest.class, attribute = "componentScanBasePackages")
    String[] componentScanBasePackages() default {};

    @AliasFor(annotation = SpringBootTest.class, attribute = "properties")
    String[] properties() default {};

}
