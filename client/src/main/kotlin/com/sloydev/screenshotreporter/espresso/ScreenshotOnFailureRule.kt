package com.sloydev.screenshotreporter.espresso

import androidx.test.espresso.Espresso
import androidx.test.espresso.getCurrentFailureHandler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

open class ScreenshotOnFailureRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                val originalHandler = getCurrentFailureHandler()
                val screenshotHandler = ScreenshotFailureHandler(originalHandler)

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
