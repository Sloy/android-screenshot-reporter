package com.sloydev.screenshotreporter.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class BaseTask : DefaultTask() {
    protected lateinit var extension: ReporterPluginExtension

    open fun init(extension: ReporterPluginExtension) {
        this.extension = extension
    }
}

open class SetupScreenshotsTask : BaseTask() {

    @TaskAction
    fun setupScreenshotReporter() {
        ScreenshotReporter(extension.getOutputFolderFile(project), extension.externalStorageFolder)
                .cleanScreenshotsFromDevice()
    }

    companion object {
        const val TASK_NAME = "setupScreenshots"
    }
}

open class PullScreenshotsTask : BaseTask() {

    @TaskAction
    fun pullScreenshots() {
        ScreenshotReporter(extension.getOutputFolderFile(project), extension.externalStorageFolder)
                .pullScreenshots()
    }

    companion object {
        const val TASK_NAME = "pullScreenshots"
    }
}