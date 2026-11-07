package com.levinzonr.compose.template.features.greeting.framework

import com.levinzonr.compose.template.features.greeting.data.datasource.PlatformDataSource

internal class NativePlatformDataSource : PlatformDataSource {
    override fun getPlatformName(): String = getPlatform().name
}
