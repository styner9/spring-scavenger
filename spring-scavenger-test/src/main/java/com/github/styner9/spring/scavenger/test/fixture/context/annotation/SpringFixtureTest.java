package com.github.styner9.spring.scavenger.test.fixture.context.annotation;

import com.github.styner9.spring.scavenger.test.fixture.context.FixtureTestDependencyFilter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration
@ComponentScan(
        includeFilters = {
                @Filter(type = FilterType.CUSTOM, classes = FixtureTestDependencyFilter.class)
        },
        useDefaultFilters = false
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Inherited
public @interface SpringFixtureTest {
    @AliasFor(annotation = ContextConfiguration.class, attribute = "classes")
    Class[] contextConfigurationClasses() default {};

    @AliasFor(annotation = ContextConfiguration.class, attribute = "initializers")
    Class<? extends ApplicationContextInitializer<?>>[] contextInitializers() default {};

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
    String[] componentScanBasePackages() default {};
}
