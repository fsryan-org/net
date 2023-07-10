package com.fsryan.util.net

import kotlin.test.Test
import kotlin.test.assertNotNull

class UserAgentTest {

    @Test
    fun shouldCreateCorrectUserAgent() {
        val userAgent = createStandardUserAgent(appName = "AppName", appVersionName = "3.4.15")
        println(userAgent)
        assertNotNull(userAgent)
    }
}