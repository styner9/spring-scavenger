package com.github.styner9.spring.scavenger.test.fixture.context.listener

import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.springframework.transaction.support.TransactionSynchronizationManager

internal class ActiveTransactionVerifier: AbstractTestExecutionListener() {

    override fun beforeTestExecution(testContext: TestContext) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw IllegalStateException("there is no active transaction")
        }
    }
}