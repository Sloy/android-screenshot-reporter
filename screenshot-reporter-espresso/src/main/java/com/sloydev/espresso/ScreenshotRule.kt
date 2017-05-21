package com.sloydev.espresso

import android.support.test.espresso.Espresso
import android.support.test.espresso.base.DefaultFailureHandler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.File


open class ScreenshotRule : TestRule {

    private lateinit var className: String
    private lateinit var methodName: String

    val screenshotsDirectory = ScreenshotDirectoryProvider.getInstance().getScreenshotsDirectory()

    override fun apply(base: Statement, description: Description): Statement {
        className = description.className
        methodName = description.methodName
        return object : Statement(){
            override fun evaluate() {
                setupFailureHandler()
                base.evaluate()
            }

        }

    }

    fun takeScreenshot(tag: String): File {
        screenshotsDirectory.mkdirs()
        val screenshotFile = getScreenshotFileFor(className, methodName, tag)
        ScreenshotAction.perform(screenshotFile)
        return screenshotFile
    }

    fun setupFailureHandler() {
        val failureHandler = ScreenshotFailureHandler(this, DefaultFailureHandler(getAppContext()))
        Espresso.setFailureHandler(failureHandler)
    }

    private fun getScreenshotFileFor(className: String, methodName: String, tag: String): File {
        val screenshotName = "${className}__${methodName}__$tag.png"
        return screenshotsDirectory.resolve(screenshotName)
    }
}
