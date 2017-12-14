package com.sloydev.screenshotreporter.espresso

import android.support.test.espresso.FailureHandler
import android.support.test.espresso.getCurrentFailureHandler
import android.view.View
import org.hamcrest.Matcher
import org.junit.runner.Description


class ScreenshotFailureHandler(private val testDescription: Description,
                               private val baseFailureHandler: FailureHandler = getCurrentFailureHandler())
    : FailureHandler {

    override fun handle(error: Throwable, viewMatcher: Matcher<View>) {
        val className = Class.forName(testDescription.className).simpleName
        val methodName = testDescription.methodName
        val screenshotName = className + "." + methodName
        Screenshot.take(name = screenshotName, suffix = " (FAILURE)", failOnError = false)

        baseFailureHandler.handle(error, viewMatcher)
    }
}