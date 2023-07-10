package com.fsryan.util.net

internal actual fun createStandardUserAgent(appName: String, appVersionName: String): String {
    return "$appName/$appVersionName ${System.getProperty("http.agent")}"
}