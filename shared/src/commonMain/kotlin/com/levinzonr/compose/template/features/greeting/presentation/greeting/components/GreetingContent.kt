package com.levinzonr.compose.template.features.greeting.presentation.greeting.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.levinzonr.compose.template.designsystem.components.AppText
import com.levinzonr.compose.template.designsystem.theme.AppTheme
import composemultiplatformtemplate.shared.generated.resources.Res
import composemultiplatformtemplate.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun GreetingContent(message: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(Res.drawable.compose_multiplatform), contentDescription = null)
        AppText(message, color = AppTheme.colors.onPrimaryContainer)
    }
}

@Preview
@Composable
private fun GreetingContentPreview() {
    AppTheme(darkTheme = false) {
        Box(modifier = Modifier.background(AppTheme.colors.primaryContainer)) {
            GreetingContent(message = "Hello, Android!")
        }
    }
}
