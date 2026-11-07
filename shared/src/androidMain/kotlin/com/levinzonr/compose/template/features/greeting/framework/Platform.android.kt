// Preserve the Platform.<platform>.kt naming convention for platform implementations.
@file:Suppress("MatchingDeclarationName")

package com.levinzonr.compose.template.features.greeting.framework

import android.os.Build

internal class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

internal actual fun getPlatform(): Platform = AndroidPlatform()
