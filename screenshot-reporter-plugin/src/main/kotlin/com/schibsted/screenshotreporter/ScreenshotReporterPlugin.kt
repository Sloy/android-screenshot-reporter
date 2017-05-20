package com.schibsted.screenshotreporter

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

open class ScreenshotReporterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        check(project.plugins.hasPlugin("com.android.application"),
                { "android-screenshot-reported can only be applied to android applications" })

        val appExtension = project.property("android") as AppExtension
        appExtension.applicationVariants.all { variant ->
            val variantSuffix = variant.name.capitalize()
            val packageName = variant.applicationId


            val testTask: Task? = variant.testVariant?.connectedInstrumentTest
            testTask?.let {
                val setupTask = project.tasks.create(SetupScreenshotReporterTask.TASK_NAME + variantSuffix, SetupScreenshotReporterTask::class.java)
                setupTask.appPackage = packageName
                project.tasks.add(setupTask)

                val reporterTask = project.tasks.create(ScreenshotReporterTask.TASK_NAME + variantSuffix, ScreenshotReporterTask::class.java)
                reporterTask.appPackage = packageName
                project.tasks.add(reporterTask)

                setupTask.dependsOn(variant.install) // install first to let us grant permissions
                testTask.dependsOn(setupTask)
                testTask.finalizedBy(reporterTask)
            }
        }


    }

}