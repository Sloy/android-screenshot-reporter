package com.sloydev.screenshotreporter.espresso

import android.content.Context
import android.os.Environment
import java.io.File

interface ScreenshotDirectoryStrategy {
    fun getScreenshotsDirectory(): File
}

class LollipopScreenshotDirectoryStrategy : ScreenshotDirectoryStrategy {
    override fun getScreenshotsDirectory() = File(Environment.getExternalStorageDirectory(), "app_test_screenshots")
}

class PreLollipopScreenshotDirectoryStrategy : ScreenshotDirectoryStrategy {
    override fun getScreenshotsDirectory(): File = getAppContext().getDir("app_test_screenshots", Context.MODE_WORLD_READABLE)

}