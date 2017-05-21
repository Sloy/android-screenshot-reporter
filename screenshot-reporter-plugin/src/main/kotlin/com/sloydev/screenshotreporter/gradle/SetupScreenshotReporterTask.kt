package com.sloydev.screenshotreporter.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


open class SetupScreenshotReporterTask : DefaultTask() {

    companion object {
        val TASK_NAME = "setupScreenshots"
    }

    lateinit var appPackage: String

    @TaskAction
    fun setupScreenshotReporter() {
        check(appPackage != null, { "appPackage parameter is not set" })

        val screenshotReporter = ScreenshotReporter(appPackage)
        screenshotReporter.grantPermissions()
        screenshotReporter.cleanScreenshotsFromDevice()
    }

}