package com.sloydev.screenshotreporter.espresso

import android.support.test.espresso.Espresso
import android.support.test.espresso.FailureHandler
import android.support.test.espresso.base.DefaultFailureHandler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.File


open class ScreenshotRule : TestRule {

    private val screenshotsDirectory = ScreenshotDirectory.get()
    private val delegateFailureHandler: FailureHandler = DefaultFailureHandler(getAppContext())

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                CurrentTestInfo.className = description.className
                CurrentTestInfo.methodName = description.methodName
                CurrentTestInfo.lastErrorFile = null
                setupFailureHandler()

                try {
                    base.evaluate()
                    CurrentTestInfo.lastErrorFile?.delete()
                } catch(e: Exception) {
                    throw e
                }
            }
        }
    }

    fun takeScreenshot(tag: String): File {
        screenshotsDirectory.mkdirs()
        val screenshotFile = getScreenshotFileFor(CurrentTestInfo.className, CurrentTestInfo.methodName, tag)
        Screenshot.take(screenshotFile, failOnError = false)
        return screenshotFile
    }

    fun setupFailureHandler() {
        Espresso.setFailureHandler({ error, viewMatcher ->
            CurrentTestInfo.lastErrorFile = takeScreenshot("ERROR")
            delegateFailureHandler.handle(error, viewMatcher)
        })
    }

    private fun getScreenshotFileFor(className: String, methodName: String, tag: String): File {
        val screenshotName = "${className}__${methodName}__$tag.png"
        return screenshotsDirectory.resolve(screenshotName)
    }
}
