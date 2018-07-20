package com.github.styner9.spring.scavenger.test.fixture.context

import com.github.styner9.spring.scavenger.test.fixture.context.listener.DependencyResolver
import org.springframework.core.type.ClassMetadata
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter

class FixtureTestDependencyFilter : AbstractClassTestingTypeFilter() {
    override fun match(metadata: ClassMetadata): Boolean {
        return DependencyResolver.contains(Class.forName(metadata.className))
    }
}