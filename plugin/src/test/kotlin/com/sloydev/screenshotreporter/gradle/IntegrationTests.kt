package com.sloydev.screenshotreporter.gradle

import com.google.common.truth.Truth.assertThat
import org.apache.commons.io.FileUtils
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

    private lateinit var projectDir: File
    private val reporterTaskName = ":${ReportScreenshotsTask.TASK_NAME}"
    private val setupTaskName = ":${SetupScreenshotsTask.TASK_NAME}"

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
        val result = gradleRunner()
                .withArguments(reporterTaskName)
                .build()

        val task = result.task(reporterTaskName)
        val taskOutcome = task?.outcome
        assertThat(taskOutcome).isEqualTo(TaskOutcome.SUCCESS)
    }

    @Test
    fun task_generates_output_files() {
        gradleRunner()
                .withArguments(reporterTaskName)
                .build()

        val outputReportDirectory = projectDir.resolve("build")
                .resolve(ReportScreenshotsTask.REPORTS_FOLDER)
                .resolve(ReportScreenshotsTask.REPORTS_SUBFOLDER)
        assertThat(outputReportDirectory.exists())
                .isTrue()
    }

    @Test
    fun custom_task_runs_all() {
        val result = gradleRunner()
                .withArguments("dummyTask")
                .build()

        val reporterTask = result.task(reporterTaskName)
        val setupTask = result.task(setupTaskName)
        val customTask = result.task(":dummyTask")

        assertThat(reporterTask?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(setupTask?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(customTask?.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }

    private fun gradleRunner(): GradleRunner {
        return GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .forwardOutput()
    }
}
