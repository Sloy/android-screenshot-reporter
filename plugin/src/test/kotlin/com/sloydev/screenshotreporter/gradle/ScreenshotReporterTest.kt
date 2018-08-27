package com.sloydev.screenshotreporter.gradle

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ScreenshotReporterTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    lateinit var outputFolder: File
    lateinit var inputTestFolder: File

    lateinit var screenshotReporter: ScreenshotReporter

    @Before
    fun setUp() {
        outputFolder = temporaryFolder.root.resolve("outputs")
        inputTestFolder = temporaryFolder.newFolder("inputs")

        screenshotReporter = ScreenshotReporter(outputFolder, externalStoragePath)
    }

    @Test
    fun `pull files when device has files`() {
        givenDeviceHasReportFiles()

        screenshotReporter.pullScreenshots()
        val exportedFiles = outputFolder.listFiles()
        val exportedFileNames = exportedFiles.map { it.name }

        assertThat(exportedFileNames)
                .containsAllOf("screenshot1.png", "screenshot2.png")
    }

    @Test
    fun `pull nothing when screenshots have been cleaned`() {
        givenDeviceHasReportFiles()
        screenshotReporter.cleanScreenshotsFromDevice()

        screenshotReporter.pullScreenshots()
        val exportedFiles = outputFolder.listFiles()

        assertThat(exportedFiles)
                .isEmpty()
    }

    @Test
    fun `delete files from output folder before reporting`() {
        givenOutputFolderHasOldFiles()

        screenshotReporter.cleanScreenshotsFromDevice()
        screenshotReporter.pullScreenshots()
        val exportedFiles = outputFolder.listFiles()

        assertThat(exportedFiles)
                .isEmpty()
    }

    @Test
    fun `creates output folder when doesn't exist`() {
        assertWithMessage("Output folder already existed")
                .that(outputFolder.exists())
                .isFalse()

        screenshotReporter.pullScreenshots()

        assertWithMessage("Output folder has not been created")
                .that(outputFolder.exists())
                .isTrue()
    }

    private fun givenOutputFolderHasOldFiles() {
        arrayOf("old_file1.png", "old_file.png")
                .map { outputFolder.resolve(it) }
                .forEach {
                    it.mkdirs()
                    it.createNewFile()
                }
    }

    private fun givenDeviceHasReportFiles() {
        pushFilesToDevice(arrayOf("screenshot1.png", "screenshot2.png"))
    }

    private fun pushFilesToDevice(files: Array<String>) {
        with(screenshotReporter.getAdb().devices[0]) {
            files.map {
                inputTestFolder.resolve(it)
                        .apply { createNewFile() }
            }.forEach {
                val deviceFile = getExternalStoragePath(this, externalStoragePath) + "/" + it.name
                println("device file: $deviceFile")
                pushFile(it.absolutePath, deviceFile)
            }
        }
    }

    companion object {
        private const val externalStoragePath = "app_spoon-screenshots"
    }
}