package com.sloydev.screenshotreporter.espresso

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import android.view.View
import java.io.File

fun getCurrentActivity(): Activity? {
    var activity: Activity? = null

    getInstrumentation().waitForIdleSync()
    getInstrumentation().runOnMainSync {
        val activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
        if (activities.isEmpty()) {
            activity = null
        } else {
            activity = Iterables.getOnlyElement(activities)
        }
    }
    return activity
}

fun getAppContext(): Context = getInstrumentation().targetContext