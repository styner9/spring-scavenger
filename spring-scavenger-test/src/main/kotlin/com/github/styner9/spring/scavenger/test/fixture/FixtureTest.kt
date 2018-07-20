package com.github.styner9.spring.scavenger.test.fixture

import com.github.styner9.spring.scavenger.test.fixture.context.FixtureTestRunner
import com.github.styner9.spring.scavenger.test.fixture.core.FixtureHolder
import com.github.styner9.spring.scavenger.test.fixture.core.Spec
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@RunWith(FixtureTestRunner::class)
@Transactional
abstract class FixtureTest<R: Any>: FixtureHolder {

    @Autowired
    protected lateinit var repository: R

    abstract fun generateFixtures(spec: Spec)

}

