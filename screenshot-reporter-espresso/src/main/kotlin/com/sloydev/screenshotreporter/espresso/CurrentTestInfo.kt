package com.sloydev.screenshotreporter.espresso

import java.io.File


object CurrentTestInfo {
    var className: String = ""
    var methodName: String = ""
    var lastErrorFile: File? = null
}