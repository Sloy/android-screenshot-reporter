package com.schibsted.screenshotreporter

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


open class ScreenshotReporterTask : DefaultTask() {

    @TaskAction
    fun reportScreenshots() {

    }

}