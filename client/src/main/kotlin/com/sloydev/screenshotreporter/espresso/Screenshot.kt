package com.sloydev.screenshotreporter.espresso

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import org.junit.Test
import java.io.File

object Screenshot {

    @JvmOverloads
    @JvmStatic
    fun take(name: String? = null, failOnError: Boolean = true) {
        val fileName = name ?: testName() ?: methodName()
        val outputFile = ScreenshotDirectory.get().resolve("$fileName.png")
        take(outputFile, failOnError)
    }

    @JvmOverloads
    @JvmStatic
    fun take(toFile: File, failOnError: Boolean = true) {
        try {
            takeOrFail(toFile)
        } catch (e: Exception) {
            if (failOnError) {
                throw e
            } else {
                Log.w("Screenshot", e)
            }
        }
    }

    private fun takeOrFail(toFile: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getAppContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                throw IllegalStateException("Can't take screenshots without WRITE_EXTERNAL_STORAGE permission")
            }
        }
        ScreenshotStrategy.get().take(toFile)
    }

    private fun methodName(): String {
        return Thread.currentThread().stackTrace[0]
                .let { it.className + "." + it.methodName }
    }

    private fun testName(): String? {
        return Thread.currentThread().stackTrace
                .find { traceElement ->
                    silentTry {
                        traceElement.let { Class.forName(it.className) }
                                .getDeclaredMethod(traceElement.methodName)
                                .getAnnotation(Test::class.java)
                    } != null
                }
                ?.let { testMethodElement ->
                    testMethodElement.className + "." + testMethodElement.methodName
                }
    }

    private fun <R> silentTry(block: () -> R): R? {
        return try {
            block()
        } catch (e: Exception) {
            null
        }
    }
}
