package com.sloydev.screenshotreporter.gradle

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertTrue
import org.junit.Test

class ScreenshotReporterPluginTest {

    @Test
    fun `adds task to project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.sloydev.screenshot-reporter")

        assertTrue(project.tasks.getByName(ScreenshotReporterTask.TASK_NAME) is ScreenshotReporterTask)
    }
}
