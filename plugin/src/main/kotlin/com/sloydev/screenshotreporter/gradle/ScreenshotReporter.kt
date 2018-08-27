package com.sloydev.screenshotreporter.gradle

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.CollectingOutputReceiver
import com.android.ddmlib.FileListingService
import com.android.ddmlib.FileListingService.TYPE_DIRECTORY
import com.android.ddmlib.IDevice
import com.android.ddmlib.SyncService
import com.android.ddmlib.SyncService.getNullProgressMonitor
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit

class ScreenshotReporter(private val outputDir: File, private val externalStorageSubfolder: String) {

    companion object {
        val SDK = File(System.getenv("ANDROID_HOME"))
        val MARSHMALLOW_API_LEVEL = 23
    }

    fun pullScreenshots() {
        outputDir.deleteRecursively()
        outputDir.mkdirs()

        val adb = getAdb()
        val singleDevice = getRunningDevice(adb)
        pullDirectory(singleDevice, externalStorageSubfolder, outputDir)
        simplifyDirectoryStructure()

        println("Wrote screenshots report to file://${outputDir.absolutePath}")
    }

    private fun simplifyDirectoryStructure() {
        outputDir.resolve(externalStorageSubfolder).listFiles().forEach { subDir ->
            subDir.renameTo(outputDir.resolve(subDir.name))
        }
        outputDir.resolve(externalStorageSubfolder).delete()
    }

    fun cleanScreenshotsFromDevice() {
        with(getRunningDevice(getAdb())) {
            val outputReceiver = CollectingOutputReceiver()
            val externalPath = getExternalStoragePath(this, externalStorageSubfolder)
            val command = "rm -rf $externalPath"

            executeShellCommand(command, outputReceiver)
        }
    }

    fun grantPermissions(appPackage: String) {
        val device = getRunningDevice(getAdb())
        val apiLevel = device.getProperty("ro.build.version.sdk").toInt()
        if (apiLevel >= MARSHMALLOW_API_LEVEL) {
            val grantOutputReceiver = CollectingOutputReceiver()
            device.executeShellCommand(
                    "pm grant $appPackage android.permission.READ_EXTERNAL_STORAGE",
                    grantOutputReceiver)
            device.executeShellCommand(
                    "pm grant $appPackage android.permission.WRITE_EXTERNAL_STORAGE",
                    grantOutputReceiver)
        }
    }

    fun getAdb(): AndroidDebugBridge {
        val adbPath = SDK.resolve("platform-tools").resolve("adb")

        AndroidDebugBridge.initIfNeeded(true)
        val adb = AndroidDebugBridge.createBridge(adbPath.absolutePath, false)

        waitForAdb(adb, Duration.ofSeconds(30))
        return adb
    }
}

private fun getRunningDevice(adb: AndroidDebugBridge): IDevice {
    val devices = adb.devices
    check(devices.isNotEmpty(), { "No devices found" })
    check(devices.size == 1, { "More than one device, not supported for now :(" })

    val singleDevice = devices[0]
    return singleDevice
}

private fun waitForAdb(adb: AndroidDebugBridge, timeOut: Duration) {
    var timeOutMs = timeOut.toMillis()
    val sleepTimeMs = TimeUnit.SECONDS.toMillis(1)
    while (!adb.hasInitialDeviceList() && timeOutMs > 0) {
        try {
            Thread.sleep(sleepTimeMs)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }

        timeOutMs -= sleepTimeMs
    }
    if (timeOutMs <= 0 && !adb.hasInitialDeviceList()) {
        throw RuntimeException("Timeout getting device list.")
    }
}

private fun pullDirectory(device: IDevice, name: String, outputDir: File) {
    // Output path on private internal storage, for KitKat and below.
    //val internalDir = getDirectoryOnInternalStorage(name)
    //println("Internal path is " + internalDir.getFullPath())

    // Output path on public external storage, for Lollipop and above.
    val externalDir: FileListingService.FileEntry = getDirectoryOnExternalStorage(device, name)
    println("External path is " + externalDir.getFullPath())
    println("Local path is " + outputDir.absolutePath)

    // Sync test output files to the local filesystem.
    println("Pulling files from external dir on [${device.serialNumber}]")
    val localDirName = outputDir.absolutePath
    adbPull(device, externalDir, localDirName)

    //println("Pulling files from internal dir on [${device.serialNumber}]")
    //adbPull(device, internalDir, localDirName)
    //println("Done pulling $name from on [${device.serialNumber}]")
}

private fun getDirectoryOnExternalStorage(device: IDevice, dir: String): FileListingService.FileEntry {
    val externalPath: String = getExternalStoragePath(device, dir)
    return obtainDirectoryFileEntry(externalPath)
}

fun getExternalStoragePath(device: IDevice, path: String): String {
    val pathNameOutputReceiver = CollectingOutputReceiver()
    device.executeShellCommand("echo \$EXTERNAL_STORAGE", pathNameOutputReceiver)
    return pathNameOutputReceiver.output.trim { it <= ' ' } + "/" + path
}

/** Get a [FileEntry] for an arbitrary path.  */
private fun obtainDirectoryFileEntry(path: String): FileListingService.FileEntry {
    // Dark magic here
    val constructor = FileListingService.FileEntry::class.java
            .getDeclaredConstructor(FileListingService.FileEntry::class.java, String::class.java, Int::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType)
    constructor.isAccessible = true

    var lastEntry: FileListingService.FileEntry? = null
    for (part in path.split("/")) {
        lastEntry = constructor.newInstance(lastEntry, part, TYPE_DIRECTORY, lastEntry == null)
    }
    return lastEntry ?: throw RuntimeException("No entries for $path")
}

private fun adbPull(device: IDevice, remoteDirName: FileListingService.FileEntry, localDirName: String) {
    getNullProgressMonitor()
    val progressMonitor: SyncService.ISyncProgressMonitor = object : SyncProgressMonitorAdapter() {
        override fun start(totalWork: Int) {
            println("-> start pulling files")
        }

        override fun startSubTask(name: String) {
            println("pulling $name ...")
        }

        override fun stop() {
            println("<- done.")
        }
    }
    device.syncService.pull(arrayOf(remoteDirName), localDirName, progressMonitor)
}

open class SyncProgressMonitorAdapter : SyncService.ISyncProgressMonitor {

    override fun startSubTask(name: String) {
    }

    override fun start(totalWork: Int) {
    }

    override fun stop() {
    }

    override fun isCanceled(): Boolean {
        return false
    }

    override fun advance(work: Int) {
    }
}