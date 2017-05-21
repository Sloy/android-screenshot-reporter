package com.sloydev.screenshotreporter.espresso

import android.app.Activity
import com.jraska.falcon.Falcon
import java.io.File


interface ScreenshotTaker {
    fun take(activity: Activity, outputFile: File)
}

class FalconScreenshotTaker : ScreenshotTaker {
    override fun take(activity: Activity, outputFile: File) {
        Falcon.takeScreenshot(activity, outputFile)
    }

}