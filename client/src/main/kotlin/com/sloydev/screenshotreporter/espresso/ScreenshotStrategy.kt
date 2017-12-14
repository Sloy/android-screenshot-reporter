package com.sloydev.screenshotreporter.espresso

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Build
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import com.jraska.falcon.Falcon
import com.sloydev.screenshotreporter.espresso.SleepViewAction.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


interface ScreenshotStrategy {
    companion object {
        fun get(): ScreenshotStrategy {
            return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                UiAutomatorScreenshotStrategy()
            } else {
                FalconScreenshotStrategy()
            }
        }
    }

    fun take(outputFile: File)
}

class FalconScreenshotStrategy : ScreenshotStrategy {
    override fun take(outputFile: File) {
        Falcon.takeScreenshot(getCurrentActivity(), outputFile)
    }
}

class UiAutomatorScreenshotStrategy : ScreenshotStrategy {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun take(outputFile: File) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            throw RuntimeException("UiAutomatorScreenshotStrategy requires API level >= 18")
        }

        // Sleep a bit to finish animations and stuff. Sorry u_u
        onView(isRoot()).perform(sleep(100))

        val uiAutomation = InstrumentationRegistry.getInstrumentation().uiAutomation
        val screenshotBitmap = uiAutomation.takeScreenshot()
        writeBitmap(screenshotBitmap, outputFile)
    }

    private fun writeBitmap(bitmap: Bitmap, toFile: File) {
        BufferedOutputStream(FileOutputStream(toFile))
                .use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
    }
}