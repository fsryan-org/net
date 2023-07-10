package com.fsryan.util.net

internal actual fun createStandardUserAgent(appName: String, appVersionName: String): String {
    val osName = System.getProperty("os.name")
    val osVersion = System.getProperty("os.version")
    val osArch= System.getProperty("os.arch")
    // TODO: there may be more to do here
    return "$appName/$appVersionName ($osName; $osVersion; $osArch)"
}