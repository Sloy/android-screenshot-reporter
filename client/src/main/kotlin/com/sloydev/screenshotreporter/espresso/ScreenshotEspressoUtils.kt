package com.sloydev.screenshotreporter.espresso

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import android.view.View
import java.io.File


fun getCurrentActivity(view: View): Activity {
    var context = view.findViewById(android.R.id.content).context
    while (context !is Activity) {
        if (context is ContextWrapper) {
            context = context.baseContext
        } else {
            throw IllegalStateException("Got a context of class "
                    + context.javaClass
                    + " and I don't know how to get the Activity from it")
        }
    }
    return context
}

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

fun getAppContext(): Context = InstrumentationRegistry.getTargetContext()

fun createDir(dir: File) {
    if (!dir.exists() && !dir.mkdirs()) {
        throw RuntimeException("Unable to create output dir: " + dir.absolutePath)
    }
}