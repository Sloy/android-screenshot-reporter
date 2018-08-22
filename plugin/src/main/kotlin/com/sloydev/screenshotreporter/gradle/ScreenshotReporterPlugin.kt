package com.sloydev.screenshotreporter.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
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
        check(project.plugins.hasPlugin("com.android.application")) {
            "screenshot-reporter plugin can only be applied to android applications"
        }
        val reporterExtension = project.extensions.create("screenshotsReporter", ReporterPluginExtension::class.java)
        project.afterEvaluate {
            val appExtension = it.property("android") as AppExtension
            appExtension.applicationVariants.all { variant: ApplicationVariant ->
                if (variant.name == reporterExtension.buildVariant) {
                    addTasks(it, variant, reporterExtension)
                }
            }
        }
    }

    private fun addTasks(project: Project, variant: ApplicationVariant, reporterExtension: ReporterPluginExtension) {
        val packageName = variant.applicationId

        val screenshotsTask: Task = reporterExtension.screenshotsTask?.let {
            project.tasks.findByName(it)
        } ?: throw IllegalStateException("Task ${reporterExtension.screenshotsTask} not found")

        val setupTask = project.createTask(
                type = SetupScreenshotsTask::class,
                name = SetupScreenshotsTask.TASK_NAME,
                description = "Setup the device to allow storing screenshots and clear any previous ones",
                dependsOn = listOf(variant.install)) // install first to let us grant permissions
                .apply {
                    appPackage = packageName
                }
        project.tasks.add(setupTask)

        val reporterTask = project.createTask(
                type = ReportScreenshotsTask::class,
                name = ReportScreenshotsTask.TASK_NAME,
                description = "Downloads screenshots from the device")
                .apply {
                    appPackage = packageName
                }
        project.tasks.add(reporterTask)

        val generateTask = project.createTask(
                type = DefaultTask::class,
                name = "generateScreenshots",
                dependsOn = listOf(setupTask, screenshotsTask, reporterTask)
        )
        project.tasks.add(generateTask)
        reporterTask.mustRunAfter(screenshotsTask)
        screenshotsTask.mustRunAfter(setupTask)
    }

    private fun <T : DefaultTask> Project.createTask(
            type: KClass<T>,
            name: String,
            group: String = GROUP,
            description: String? = null,
            dependsOn: List<Task>? = null,
            finalizedBy: List<Task>? = null
    )
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