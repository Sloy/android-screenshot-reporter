package com.sloydev.screenshotreporter.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


open class PullScreenshotsTask : DefaultTask() {

    companion object {
        val REPORTS_FOLDER = "reports"
        val REPORTS_SUBFOLDER = "screenshots"
        val TASK_NAME = "pullScreenshots"
    }

    lateinit var appPackage: String

    @TaskAction
    fun pullScreenshots() {
        check(appPackage != null, { "appPackage parameter is not set" })

        ScreenshotReporter(appPackage)
                .pullScreenshots(project.buildDir.resolve(REPORTS_FOLDER).resolve(REPORTS_SUBFOLDER))
    }

}