package com.sloydev.screenshotreporter.gradle

import com.android.build.gradle.AppExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import kotlin.reflect.KClass

open class ScreenshotReporterPlugin : Plugin<Project> {

    companion object {
        const val GROUP = "Screenshots Reporter"
    }

    override fun apply(project: Project) {
        check(project.plugins.hasPlugin("com.android.application"),
                { "screenshot-reporter plugin can only be applied to android applications" })

        val appExtension = project.property("android") as AppExtension
        appExtension.applicationVariants.all { variant ->
            val variantSuffix = variant.name.capitalize()
            val packageName = variant.applicationId


            val testTask: Task? = variant.testVariant?.connectedInstrumentTest
            testTask?.let {
                val setupTask = project.createTask(
                        type = SetupScreenshotReporterTask::class,
                        name = SetupScreenshotReporterTask.TASK_NAME + variantSuffix,
                        description = "Setup the device to allow storing screenshots and clear any previous ones",
                        dependsOn = listOf(variant.install)) // install first to let us grant permissions
                        .apply {
                            appPackage = packageName
                        }
                project.tasks.add(setupTask)

                val reporterTask = project.createTask(
                        type = ScreenshotReporterTask::class,
                        name = ScreenshotReporterTask.TASK_NAME + variantSuffix,
                        description = "Downloads screenshots from the device")
                        .apply {
                            appPackage = packageName
                        }
                project.tasks.add(reporterTask)

                testTask.finalizedBy(reporterTask)
                testTask.dependsOn(setupTask)
            }
        }


    }

    private fun <T : DefaultTask> Project.createTask(type: KClass<T>,
                                                     name: String,
                                                     group: String = GROUP,
                                                     description: String? = null,
                                                     dependsOn: List<Task>? = null,
                                                     finalizedBy: List<Task>? = null)
            : T {
        return type.java.cast(project.tasks.create(LinkedHashMap<String, Any>().apply {
            put("name", name)
            put("type", type.java)
            put("group", group)
            description?.let { put("description", it) }
            dependsOn?.let { put("dependsOn", it) }
            finalizedBy?.let { put("finalizedBy", it) }
        }))
    }

}