package com.github.styner9.spring.scavenger.test.support

import org.junit.Assert.*
import org.junit.Test

class ReflectionUtilTest {

    @Test
    fun getTypeArgumentOfGenericSuperclass() {
        assertEquals(String::class.java, ReflectionUtil.getTypeArgumentOfGenericSuperclass(Baz::class.java))
    }

    @Test
    fun getTypeArgumentOfGenericInterfaces() {
        assertEquals(Integer::class.java, ReflectionUtil.getTypeArgumentOfGenericInterfaces(Baz::class.java, Bar::class.java))
    }

    @Test
    fun getDeclaredFields() {
        val fields = ReflectionUtil.getDeclaredFields(Baz::class.java) { it.type == List::class.java }
        assertEquals(1, fields.size)
        fields[0].apply {
            assertEquals("baz", name)
            assertEquals(List::class.java, type)
        }
    }

    abstract class Foo<T> {
        private val foo = listOf(1)
    }

    interface Bar<T>

    class Baz: Foo<String>(), Bar<Int> {
        private val baz = listOf(1)
    }

}