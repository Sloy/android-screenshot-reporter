package com.schibsted.screenshotreporter

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertTrue
import org.junit.Test


class ScreenshotReporterPluginTest {

    @Test
    fun greeterPluginAddsGreetingTaskToProject() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.schibsted.android-screenshot-reporter")

        assertTrue(project.tasks.getByName(ScreenshotReporterPlugin.TASK_NAME) is ScreenshotReporterTask)
    }

}
