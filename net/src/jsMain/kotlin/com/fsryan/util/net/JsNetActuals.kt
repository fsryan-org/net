package com.fsryan.util.net

// TODO: see this
// import org.w3c.dom.Navigator

internal actual fun createStandardUserAgent(appName: String, appVersionName: String): String {
    // TODO: make this work, gathering the info from the navigator possibly?
    return "$appName/$appVersionName (js)"
}