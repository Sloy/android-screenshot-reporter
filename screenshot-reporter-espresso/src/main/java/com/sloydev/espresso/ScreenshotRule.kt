package com.sloydev.espresso

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


open class ScreenshotRule : TestRule {

    private var className: String? = null
    private var methodName: String? = null

    override fun apply(base: Statement, description: Description): Statement {
        className = description.className
        methodName = description.methodName
        return base
    }

    fun takeScreenshot(tag: String) {
        val screenshotsDirectory = ScreenshotDirectoryProvider.getInstance().getScreenshotsDirectory()
        screenshotsDirectory.mkdirs()

        val screenshotName = "${className}__${methodName}__$tag.png"
        val screenshotFile = screenshotsDirectory.resolve(screenshotName)

        ScreenshotAction.perform(screenshotFile)
    }

}