package com.levinzonr.compose.template.features.greeting.presentation.greeting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.levinzonr.compose.template.designsystem.components.AppButton
import com.levinzonr.compose.template.designsystem.theme.AppTheme
import com.levinzonr.compose.template.features.greeting.presentation.greeting.components.GreetingContent

@Composable
fun GreetingScreen(
    state: GreetingState,
    onAction: (GreetingAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(
            AppTheme.colors.primaryContainer
        ).safeContentPadding().fillMaxSize().padding(AppTheme.dimensions.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppButton(text = "Click me!", onClick = { onAction(GreetingAction.ToggleContent) })
        AnimatedVisibility(state.showContent) { GreetingContent(message = state.message) }
        AppButton(text = "Platform details", onClick = { onAction(GreetingAction.OpenDetails) })
    }
}

@Preview
@Composable
private fun GreetingScreenPreview() {
    AppTheme(darkTheme = false) { GreetingScreen(state = GreetingState(), onAction = {}) }
}

@Preview
@Composable
private fun GreetingScreenExpandedPreview() {
    AppTheme(darkTheme = false) {
        GreetingScreen(
            state = GreetingState(
                message = "Hello, Android!",
                showContent = true
            ),
            onAction = {
            }
        )
    }
}
