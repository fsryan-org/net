package com.fsryan.util.net

actual internal fun createStandardUserAgent(appName: String, appVersionName: String): String {
    return "$appName/$appVersionName ${System.getProperty("http.agent")}"
}