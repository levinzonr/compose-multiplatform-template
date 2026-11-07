plugins {
    base
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

spotless {
    kotlin {
        target("shared/src/**/*.kt", "androidApp/src/**/*.kt", "designsystem/src/**/*.kt")
        ktlint(libs.versions.ktlint.get())
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target(
            "*.gradle.kts",
            "shared/*.gradle.kts",
            "androidApp/*.gradle.kts",
            "designsystem/*.gradle.kts"
        )
        ktlint(libs.versions.ktlint.get())
        trimTrailingWhitespace()
        endWithNewline()
    }
}

// Analyze every platform source set without coupling Detekt to Android's Gradle DSL.
detekt {
    source.setFrom(files("shared/src", "androidApp/src", "designsystem/src"))
    config.setFrom(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

tasks.named("check") {
    dependsOn("spotlessCheck", "detekt")
}
