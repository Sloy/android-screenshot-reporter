package com.sloydev.screenshotreporter.espresso

import android.support.test.espresso.FailureHandler
import android.support.test.espresso.getCurrentFailureHandler
import android.view.View
import org.hamcrest.Matcher


class ScreenshotFailureHandler(private val baseFailureHandler: FailureHandler = getCurrentFailureHandler())
    : FailureHandler {

    override fun handle(error: Throwable, viewMatcher: Matcher<View>) {
        Screenshot.take(name = "FAILURE", failOnError = false)

        baseFailureHandler.handle(error, viewMatcher)
    }
}