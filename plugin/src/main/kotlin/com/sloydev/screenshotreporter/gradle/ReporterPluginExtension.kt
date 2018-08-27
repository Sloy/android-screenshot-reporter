package com.sloydev.screenshotreporter.gradle

import org.gradle.api.Project
import java.io.File

open class ReporterPluginExtension {
    var screenshotsTask = "connectedAndroidTest"
    var outputFolder = "screenshots"
    var externalStorageFolder = "Pictures/screenshots"

    fun getOutputFolderFile(project: Project) = File(reportsFolder(project), outputFolder)

    private fun reportsFolder(project: Project) = project.buildDir.resolve("reports")
}