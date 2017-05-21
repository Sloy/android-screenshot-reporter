package com.sloydev.screenshotreporter.espresso

import android.support.test.espresso.FailureHandler
import android.view.View
import org.hamcrest.Matcher


class ScreenshotFailureHandler(val rule: ScreenshotRule, val delegate: FailureHandler) : FailureHandler {

    override fun handle(error: Throwable, viewMatcher: Matcher<View>) {
        rule.takeScreenshot("ERROR")
        delegate.handle(error, viewMatcher)
    }
}