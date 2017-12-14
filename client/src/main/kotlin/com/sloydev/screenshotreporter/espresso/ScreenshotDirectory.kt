package com.sloydev.screenshotreporter.espresso

import android.os.Build
import java.io.File

object ScreenshotDirectory {

    fun get(): File {
        val strategy = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LollipopScreenshotDirectoryStrategy()
        } else {
            PreLollipopScreenshotDirectoryStrategy()
        }
        return strategy.getScreenshotsDirectory()
    }
}