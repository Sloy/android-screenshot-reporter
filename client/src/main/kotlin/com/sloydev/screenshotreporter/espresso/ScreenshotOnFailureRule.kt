package com.sloydev.screenshotreporter.espresso

import android.support.test.espresso.Espresso
import android.support.test.espresso.getCurrentFailureHandler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

open class ScreenshotOnFailureRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                val originalHandler = getCurrentFailureHandler()
                val screenshotHandler = ScreenshotFailureHandler(description, originalHandler)

                Espresso.setFailureHandler(screenshotHandler)
                try {
                    base.evaluate()
                } finally {
                    Espresso.setFailureHandler(originalHandler)
                }
            }
        }
    }
}
