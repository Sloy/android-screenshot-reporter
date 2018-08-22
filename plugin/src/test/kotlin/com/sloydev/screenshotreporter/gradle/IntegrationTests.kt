package com.sloydev.screenshotreporter.gradle

import com.google.common.truth.Truth.assertThat
import org.apache.commons.io.FileUtils
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
    private val reporterTaskName = ":${ReportScreenshotsTask.TASK_NAME}Debug"

    @Before
    fun setUp() {
        projectDir = temporaryFolder.newFolder("vanilla").also {
            FileUtils.copyDirectory(File(javaClass.classLoader.getResource("vanilla").path), it)
            File(it, "local.properties").writeText("sdk.dir=${System.getenv("HOME")}/Library/Android/sdk", Charsets.UTF_8)
            File(it, "libs").also {
                it.mkdir()
                FileUtils.copyFileToDirectory(File(".", "build/libs/plugin.jar"), it)
            }
        }
    }

    @Test
    fun task_runs() {
        val result: BuildResult = runTask()

        val task = result.task(reporterTaskName)
        val taskOutcome = task?.outcome
        assertThat(taskOutcome).isEqualTo(TaskOutcome.SUCCESS)
    }

    @Test
    fun task_generates_output_files() {
        runTask()

        val outputReportDirectory = projectDir.resolve("build")
                .resolve(ReportScreenshotsTask.REPORTS_FOLDER)
                .resolve(ReportScreenshotsTask.REPORTS_SUBFOLDER)
        assertThat(outputReportDirectory.exists())
                .isTrue()
    }

    private fun runTask(): BuildResult {
        val runner = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments(reporterTaskName)
                .forwardOutput()
        return runner.build()
    }
}
