package com.schibsted.screenshotreporter

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.schibsted.screenshotreporter.ScreenshotReporter.Companion.DEVICE_SCREENSHOT_DIR
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

    val screenshotReporter = ScreenshotReporter()

    @Before
    fun setUp() {
        outputFolder = temporaryFolder.newFolder("outputs")
        inputTestFolder = temporaryFolder.newFolder("inputs")
    }

    @Test
    fun `returns working adb`() {
        val adb = screenshotReporter.getAdb()

        assertWithMessage("Adb is not connected")
                .that(adb.isConnected)
                .isTrue()
    }

    @Test
    fun `returns devices`() {
        val adb = screenshotReporter.getAdb()
        val devices = adb.devices

        assertWithMessage("No devices found")
                .that(devices.asList())
                .isNotEmpty()

        devices.forEach { println(it) }
    }

    @Test
    fun `pull files when device has files`() {
        givenDeviceHasReportFiles()

        screenshotReporter.reportScreenshots(outputFolder)
        val exportedFiles = outputFolder.resolve(DEVICE_SCREENSHOT_DIR).listFiles()
        val exportedFileNames = exportedFiles.map { it.name }

        assertThat(exportedFileNames)
                .containsAllOf("screenshot1.png", "screenshot2.png")
    }

    @Test
    fun `pull nothing when screenshots have been cleaned`() {
        givenDeviceHasReportFiles()
        screenshotReporter.cleanScreenshotsFromDevice()

        screenshotReporter.reportScreenshots(outputFolder)
        val exportedFiles = outputFolder.resolve(DEVICE_SCREENSHOT_DIR).listFiles()

        assertThat(exportedFiles)
                .isEmpty()
    }

    @Test
    fun `delete files from output folder before reporting`() {
        givenOutputFolderHasOldFiles()

        screenshotReporter.cleanScreenshotsFromDevice()
        screenshotReporter.reportScreenshots(outputFolder)
        val exportedFiles = outputFolder.resolve(DEVICE_SCREENSHOT_DIR).listFiles()

        assertThat(exportedFiles)
                .isEmpty()
    }

    private fun givenOutputFolderHasOldFiles() {
        arrayOf("old_file1.png", "old_file.png")
                .map { outputFolder.resolve(DEVICE_SCREENSHOT_DIR).resolve(it) }
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
                val deviceFile = getExternalStoragePath(this, DEVICE_SCREENSHOT_DIR) + "/" + it.name
                println("device file: $deviceFile")
                pushFile(it.absolutePath, deviceFile)
            }
        }
    }

}