package com.levinzonr.compose.template.features.greeting.presentation.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.levinzonr.compose.template.designsystem.components.AppText
import com.levinzonr.compose.template.designsystem.theme.AppTheme

@Composable
internal fun PlatformInfo(platformName: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        AppText("Running on", style = AppTheme.typography.titleMedium)
        AppText(platformName, style = AppTheme.typography.bodyLarge)
    }
}

@Preview
@Composable
private fun PlatformInfoPreview() {
    AppTheme(darkTheme = false) { PlatformInfo(platformName = "iOS 26") }
}
