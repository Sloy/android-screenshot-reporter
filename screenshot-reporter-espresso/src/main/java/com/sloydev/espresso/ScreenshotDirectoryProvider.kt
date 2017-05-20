package com.sloydev.espresso

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

sealed class ScreenshotDirectoryProvider {

    companion object {
        fun getInstance() =
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> LollipopScreenshotDirectoryProvider()
                    else -> PreLollipopScreenshotDirectoryProvider()
                }
    }

    abstract fun getScreenshotsDirectory(): File
}


class LollipopScreenshotDirectoryProvider : ScreenshotDirectoryProvider() {
    override fun getScreenshotsDirectory() = File(Environment.getExternalStorageDirectory(), "app_test_screenshots")
}

class PreLollipopScreenshotDirectoryProvider : ScreenshotDirectoryProvider() {
    override fun getScreenshotsDirectory(): File = getAppContext().getDir("app_test_screenshots", Context.MODE_WORLD_READABLE)

}