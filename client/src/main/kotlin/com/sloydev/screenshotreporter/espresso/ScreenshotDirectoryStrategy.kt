package com.sloydev.screenshotreporter.espresso

import android.content.Context
import android.os.Environment
import java.io.File

const val DEVICE_SCREENSHOT_DIR = "app_spoon-screenshots"

interface ScreenshotDirectoryStrategy {
    fun getScreenshotsDirectory(): File
}

class LollipopScreenshotDirectoryStrategy : ScreenshotDirectoryStrategy {
    override fun getScreenshotsDirectory() = File(Environment.getExternalStorageDirectory(), DEVICE_SCREENSHOT_DIR)
}

class PreLollipopScreenshotDirectoryStrategy : ScreenshotDirectoryStrategy {
    override fun getScreenshotsDirectory(): File = getAppContext().getDir(DEVICE_SCREENSHOT_DIR, Context.MODE_WORLD_READABLE)

}