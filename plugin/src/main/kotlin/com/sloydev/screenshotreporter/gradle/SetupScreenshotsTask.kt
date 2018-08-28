package com.sloydev.screenshotreporter.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class SetupScreenshotsTask : DefaultTask() {

    companion object {
        val TASK_NAME = "setupScreenshots"
    }

    lateinit var appPackage: String
    lateinit var sdkDirectory: File

    @TaskAction
    fun setupScreenshotReporter() {
        check(appPackage != null, { "appPackage parameter is not set" })

        val screenshotReporter = ScreenshotReporter(appPackage, sdkDirectory)
        screenshotReporter.grantPermissions()
        screenshotReporter.cleanScreenshotsFromDevice()
    }

}