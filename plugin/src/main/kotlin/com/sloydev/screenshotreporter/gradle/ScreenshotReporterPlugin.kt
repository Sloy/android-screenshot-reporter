package com.sloydev.screenshotreporter.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

open class ScreenshotReporterPlugin : Plugin<Project> {

    companion object {
        const val GROUP = "Screenshots Reporter"
    }

    override fun apply(project: Project) {
        check(project.plugins.hasPlugin("com.android.application")) {
            "screenshot-reporter plugin can only be applied to android applications"
        }
        val reporterExtension = project.extensions.create("screenshotsReporter", ReporterPluginExtension::class.java)
        project.afterEvaluate {
            val appExtension = it.property("android") as AppExtension
            appExtension.applicationVariants.all { variant: ApplicationVariant ->
                if (variant.name == reporterExtension.buildVariant) {
                    addTasks(it, variant, reporterExtension, appExtension)
                }
            }
        }
    }

    private fun addTasks(
            project: Project,
            variant: ApplicationVariant,
            reporterExtension: ReporterPluginExtension,
            appExtension: AppExtension
    ) {
        val packageName = variant.applicationId

        val screenshotsTask: Task = reporterExtension.screenshotsTask?.let {
            project.tasks.findByName(it)
        } ?: throw IllegalStateException("Task ${reporterExtension.screenshotsTask} not found")

        val setupTask = project.tasks.register(SetupScreenshotsTask.TASK_NAME, SetupScreenshotsTask::class.java) {
            it.group = GROUP
            it.description = "Setup the device to allow storing screenshots and clear any previous ones"
            it.dependsOn(variant.installProvider)
            it.appPackage = packageName
            it.sdkDirectory = appExtension.sdkDirectory
        }

        val pullTask = project.tasks.register(PullScreenshotsTask.TASK_NAME, PullScreenshotsTask::class.java) {
            it.group = GROUP
            it.description = "Downloads screenshots from the device"
            it.mustRunAfter(screenshotsTask)
            it.appPackage = packageName
            it.sdkDirectory = appExtension.sdkDirectory
        }

        project.tasks.register("generateScreenshots") {
            it.group = GROUP
            it.dependsOn(setupTask, screenshotsTask, pullTask)
        }

        screenshotsTask.mustRunAfter(setupTask)
    }
}