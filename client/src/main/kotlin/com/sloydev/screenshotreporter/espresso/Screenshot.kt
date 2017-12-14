package com.sloydev.screenshotreporter.espresso

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.io.File

object Screenshot {

    @JvmOverloads
    @JvmStatic
    fun take(name: String? = null, failOnError: Boolean = true): File {
        val outputFile = bestOutputFile(name)
        take(outputFile, failOnError)
        return outputFile
    }

    private fun bestOutputFile(name: String?): File {
        return ScreenshotDirectory.get() + (testScreenshotFile(name)?: nonTestFile(name))
    }

    @JvmStatic
    private fun take(toFile: File, failOnError: Boolean = true) {
        try {
            takeOrFail(toFile)
        } catch (e: Exception) {
            if (failOnError) {
                throw e
            } else {
                Log.w("Screenshot", e)
            }
        }
    }

    private fun takeOrFail(outputFile: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getAppContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                throw IllegalStateException("Can't take screenshots without WRITE_EXTERNAL_STORAGE permission")
            }
        }
        outputFile.parentFile.mkdirs()

        ScreenshotStrategy.get().take(outputFile)
    }

    /**
     * When taking screenshots from a test, will return a File compatible with Spoon directory structure.
     */
    private fun testScreenshotFile(customName: String?): File? {
        val currentTest = findCurrentTest() ?: return null

        val fileName = customName ?: currentTest.methodName
        val filePath = "${currentTest.className}/${currentTest.methodName}/$fileName.png"
        return File(filePath)
    }

    private fun nonTestFile(customName: String?): File {
        val fileName = customName ?: System.currentTimeMillis()
        return File("$fileName.png")
    }


}
