package com.fsryan.util.net

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithCString
import platform.UIKit.UIDevice
import platform.posix.uname
import platform.posix.utsname

actual internal fun createStandardUserAgent(appName: String, appVersionName: String): String {
    val deviceVersion = "${UIDevice.currentDevice.systemName}/${UIDevice.currentDevice.systemVersion}"
    val dictionary = NSBundle.bundleWithIdentifier("com.apple.CFNetwork")?.infoDictionary
    val cfNetworkVerisonCode = dictionary?.get("CFBundleShortVersionString") as? String
    val cfNetworkVersion = "CFNetwork/${cfNetworkVerisonCode ?: ""}"

    val (deviceName, darwinVersion) = memScoped {
        val systemInfo: utsname = alloc()
        uname(systemInfo.ptr)
        val dName = NSString.stringWithCString(systemInfo.machine, encoding = NSUTF8StringEncoding) ?: "---"
        val darwinVersion = NSString.stringWithCString(systemInfo.release, encoding = NSUTF8StringEncoding) ?: "---"
        dName to darwinVersion
    }
    return "$appName/$appVersionName $deviceName $deviceVersion $cfNetworkVersion Darwin/$darwinVersion"
}