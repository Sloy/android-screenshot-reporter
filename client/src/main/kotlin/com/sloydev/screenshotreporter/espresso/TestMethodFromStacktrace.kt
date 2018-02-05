package com.sloydev.screenshotreporter.espresso

import org.junit.Test


data class ClassAndMethod(val className: String, val methodName: String){
    val simpleClassName
        get() = className.substringAfterLast(".")
}



fun findCurrentTest(): ClassAndMethod? {
    return Thread.currentThread().stackTrace
            .find { it.isTestMethod() }
            ?.classAndMethod()
}

private fun StackTraceElement.isTestMethod(): Boolean {
    return silentTry {
        Class.forName(className)
                .getDeclaredMethod(methodName)
                .getAnnotation(Test::class.java)
    } != null
}

private fun StackTraceElement.classAndMethod(): ClassAndMethod {
    return ClassAndMethod(className, methodName)
}

private fun <R> silentTry(block: () -> R): R? {
    return try {
        block()
    } catch (e: Exception) {
        null
    }
}
