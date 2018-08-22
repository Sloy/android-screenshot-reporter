package com.sloydev.screenshotreporter.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


open class ReportScreenshotsTask : DefaultTask() {

    companion object {
        val REPORTS_FOLDER = "reports"
        val REPORTS_SUBFOLDER = "screenshots"
        val TASK_NAME = "reportScreenshots"
    }

    lateinit var appPackage: String

    @TaskAction
    fun reportScreenshots() {
        check(appPackage != null, { "appPackage parameter is not set" })

        ScreenshotReporter(appPackage)
                .reportScreenshots(project.buildDir.resolve(REPORTS_FOLDER).resolve(REPORTS_SUBFOLDER))
    }

}