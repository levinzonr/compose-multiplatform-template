package com.levinzonr.compose.template.features.greeting.framework

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformTest {
    @Test
    fun providesNativePlatformName() {
        assertTrue(getPlatform().name.startsWith("Android "))
    }
}
