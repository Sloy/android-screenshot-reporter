package com.schibsted.screenshotreporter

import org.gradle.api.Plugin
import org.gradle.api.Project

open class ScreenshotReporterPlugin : Plugin<Project> {

    companion object {
        val TASK_NAME = "reportScreenshots"
    }

    override fun apply(project: Project) {
        val notifierTask = project.tasks.create(TASK_NAME, ScreenshotReporterTask::class.java)
        project.tasks.add(notifierTask)
    }

}