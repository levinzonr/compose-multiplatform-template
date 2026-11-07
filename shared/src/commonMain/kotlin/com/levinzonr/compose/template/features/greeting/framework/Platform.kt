package com.levinzonr.compose.template.features.greeting.framework

internal interface Platform {
    val name: String
}

internal expect fun getPlatform(): Platform
