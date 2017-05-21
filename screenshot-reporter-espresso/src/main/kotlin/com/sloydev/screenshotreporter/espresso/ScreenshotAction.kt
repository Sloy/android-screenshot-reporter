package com.sloydev.screenshotreporter.espresso

import android.content.pm.PackageManager
import android.os.Build
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.util.Log
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import java.io.File

class ScreenshotAction(val file: File, val screenshotTaker: ScreenshotTaker, val failOnError: Boolean = false) : ViewAction {

    companion object {
        fun perform(file: File, screenshotTaker: ScreenshotTaker = FalconScreenshotTaker()) {
            onView(isRoot()).perform(ScreenshotAction(file, screenshotTaker))
        }
    }

    override fun getConstraints(): Matcher<View> {
        return Matchers.any(View::class.java)
    }

    override fun getDescription(): String {
        return "Taking a screenshot."
    }

    override fun perform(uiController: UiController, view: View) {
        val activity = getCurrentActivity(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                val errorMessage = "Can't take screenshots without WRITE_EXTERNAL_STORAGE permission"
                if (failOnError) {
                    throw IllegalStateException(errorMessage)
                } else {
                    Log.e("ScreenshotReporter", errorMessage)
                    return
                }
            }
        }
        screenshotTaker.take(activity, file)
    }


}