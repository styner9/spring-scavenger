package com.github.styner9.spring.scavenger.context

class DeployPhase private constructor() {

    companion object {

        var current: String? = null
            @JvmStatic
            get() = field ?: throw IllegalStateException("not initialized")
            internal set(value: String?) {
                when {
                    (field == null).xor(value == null) -> field = value // normal & test case
                    field == value -> Unit
                    else -> throw IllegalStateException("initialized already")
                }
            }

        @JvmStatic
        fun active(phase: String) = phase == current

        @JvmStatic
        fun reset() {
            current = null
        }
    }
}