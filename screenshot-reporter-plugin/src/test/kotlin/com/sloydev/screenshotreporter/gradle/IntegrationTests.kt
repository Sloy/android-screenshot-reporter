package com.schibsted.screenshotreporter

import com.google.common.truth.Truth.assertThat
import com.sloydev.screenshotreporter.gradle.ScreenshotReporter
import com.sloydev.screenshotreporter.gradle.ScreenshotReporterTask
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File


class IntegrationTests {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    lateinit var projectDir: File
    lateinit var buildFile: File

    @Before
    fun setUp() {
        projectDir = temporaryFolder.root
        buildFile = temporaryFolder.newFile("build.gradle")
    }

    @Test
    fun task_runs() {
        givenBuildFileWithPlugin()

        val result: BuildResult = runTask()

        val task = result.task(":${ScreenshotReporterTask.TASK_NAME}")
        val taskOutcome = task.outcome
        assertThat(taskOutcome).isEqualTo(TaskOutcome.SUCCESS)
    }

    @Test
    fun task_generates_output_files() {
        givenBuildFileWithPlugin()

        runTask()

        val outputReportDirectory = projectDir.resolve("build")
                .resolve(ScreenshotReporterTask.REPORTS_FOLDER)
                .resolve(ScreenshotReporterTask.REPORTS_SUBFOLDER)
                .resolve(ScreenshotReporter.DEVICE_SCREENSHOT_DIR)
        assertThat(outputReportDirectory.exists())
                .isTrue()

    }

    private fun givenBuildFileWithPlugin() {
        buildFile.writeText(
                """plugins {
                                id 'com.sloydev.screenshot-reporter'
                            }
                    """
        )
    }

    private fun runTask(): BuildResult {
        val runner = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments(ScreenshotReporterTask.TASK_NAME)
        val result = runner.build()
        println(result.output)
        return result
    }

}
