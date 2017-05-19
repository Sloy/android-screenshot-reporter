package com.schibsted.screenshotreporter

import com.google.common.truth.Truth.assertThat
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
        buildFile.writeText(
                """plugins {
                            id 'com.schibsted.gradle-screenshot-reporter'
                        }
                """
        )

        val runner = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments(ScreenshotReporterPlugin.TASK_NAME)

        val result = runner.build()

        println(result.output)

        val task = result.task(":${ScreenshotReporterPlugin.TASK_NAME}")
        val taskOutcome = task.outcome
        assertThat(taskOutcome).isEqualTo(TaskOutcome.SUCCESS)
    }

}
