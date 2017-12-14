package com.sloydev.screenshotreporter.espresso

import android.content.pm.PackageManager
import android.os.Build
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.util.Log
import com.sloydev.screenshotreporter.espresso.SleepViewAction.sleep
import org.junit.Test
import java.io.File

object Screenshot {

    @JvmOverloads
    @JvmStatic
    fun take(name: String? = null, suffix: String = "", failOnError: Boolean = true) {
        val fileName = (name ?: testName() ?: methodName())
        val outputFile = ScreenshotDirectory.get().resolve("${fileName+suffix}.png")
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

    private fun takeOrFail(outputFile: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getAppContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                throw IllegalStateException("Can't take screenshots without WRITE_EXTERNAL_STORAGE permission")
            }
        }
        outputFile.parentFile.mkdirs()

        ScreenshotStrategy.get().take(outputFile)
    }

    private fun methodName(): String {
        val originalMethodDepth = 6
        return Thread.currentThread().stackTrace[originalMethodDepth].simpleName()
    }

    private fun testName(): String? {
        return Thread.currentThread().stackTrace
                .find { it.isTestMethod() }
                ?.simpleName()
    }

    private fun StackTraceElement.isTestMethod(): Boolean {
        return silentTry {
            Class.forName(className)
                    .getDeclaredMethod(methodName)
                    .getAnnotation(Test::class.java)
        } != null
    }

    private fun StackTraceElement.simpleName(): String {
        return Class.forName(className).simpleName + "." + methodName
    }

    private fun <R> silentTry(block: () -> R): R? {
        return try {
            block()
        } catch (e: Exception) {
            null
        }
    }
}
