package com.levinzonr.compose.template.features.greeting.presentation.details

import androidx.compose.foundation.layout.Arrangement
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
import com.levinzonr.compose.template.features.greeting.presentation.details.components.PlatformInfo

@Composable
fun GreetingDetailsScreen(
    state: GreetingDetailsState,
    onAction: (GreetingDetailsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.safeContentPadding().fillMaxSize().padding(
            AppTheme.dimensions.screenPadding
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            AppTheme.dimensions.contentGap,
            Alignment.CenterVertically
        )
    ) {
        PlatformInfo(platformName = state.platformName)
        AppButton(text = "Back", onClick = { onAction(GreetingDetailsAction.Back) })
    }
}

@Preview
@Composable
private fun GreetingDetailsScreenPreview() {
    AppTheme(darkTheme = false) {
        GreetingDetailsScreen(state = GreetingDetailsState(platformName = "iOS 26"), onAction = {})
    }
}
