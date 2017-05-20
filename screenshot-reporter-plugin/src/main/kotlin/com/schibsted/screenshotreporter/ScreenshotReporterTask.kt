package com.schibsted.screenshotreporter

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


open class ScreenshotReporterTask : DefaultTask() {

    companion object {
        val REPORTS_FOLDER = "reports"
        val REPORTS_SUBFOLDER = "screenshots"
    }
    @TaskAction
    fun reportScreenshots() {
        ScreenshotReporter()
                .reportScreenshots(project.buildDir.resolve(REPORTS_FOLDER).resolve(REPORTS_SUBFOLDER))
    }

}