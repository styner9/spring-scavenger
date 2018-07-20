package com.github.styner9.spring.scavenger.test.support

import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object ReflectionUtil {

    @JvmOverloads
    fun getTypeArgumentOfGenericSuperclass(clazz: Class<*>, idx: Int = 0): Class<*>? {
        return getTypeArgument(arrayOf(clazz.genericSuperclass), clazz.superclass, idx)
    }

    @JvmOverloads
    fun getTypeArgumentOfGenericInterfaces(clazz: Class<*>, targetInterfaceClazz: Class<*>, idx: Int = 0): Class<*>? {
        return getTypeArgument(clazz.genericInterfaces, targetInterfaceClazz, idx)
    }

    private fun getTypeArgument(types: Array<Type>, rawType: Class<*>, idx: Int): Class<*>? {
        for (type in types) {
            if (type is ParameterizedType && type.rawType == rawType) {
                return type.actualTypeArguments[idx] as Class<*>
            }
        }
        return null
    }

    fun getDeclaredFields(declaringClazz: Class<*>, filter: (Field) -> Boolean): List<Field> {
        return declaringClazz.declaredFields.filter { filter.invoke(it) }
    }
}