// Preserve the Platform.<platform>.kt naming convention for platform implementations.
@file:Suppress("MatchingDeclarationName")

package com.levinzonr.compose.template.features.greeting.framework

import platform.UIKit.UIDevice

internal class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

internal actual fun getPlatform(): Platform = IOSPlatform()
