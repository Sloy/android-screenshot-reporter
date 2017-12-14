package com.sloydev.screenshotreporter.espresso

import java.io.File

/**
 * Alias for Java code
 */
fun deleteDirectory(dir: File) {
    dir.deleteRecursively()
}

operator fun File.plus(file: File): File = resolve(file)