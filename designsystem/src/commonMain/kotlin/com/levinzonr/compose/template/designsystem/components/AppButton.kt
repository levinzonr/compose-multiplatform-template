package com.levinzonr.compose.template.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.levinzonr.compose.template.designsystem.theme.AppTheme

/** Shared primary action. Material supplies interaction semantics and disabled-state behavior. */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = AppTheme.dimensions.buttonMinHeight),
        enabled = enabled,
        shape = AppTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.primary,
            contentColor = AppTheme.colors.onPrimary
        ),
        contentPadding = PaddingValues(
            horizontal = AppTheme.dimensions.buttonHorizontalPadding,
            vertical = AppTheme.dimensions.buttonVerticalPadding
        )
    ) {
        Text(text = text, style = AppTheme.typography.labelLarge)
    }
}

@Preview
@Composable
private fun AppButtonPreview() {
    AppTheme(darkTheme = false) { AppButton(text = "Continue", onClick = {}) }
}

@Preview
@Composable
private fun AppButtonDisabledPreview() {
    AppTheme(darkTheme = false) { AppButton(text = "Continue", onClick = {}, enabled = false) }
}

@Preview
@Composable
private fun AppButtonDarkPreview() {
    AppTheme(darkTheme = true) { AppButton(text = "Continue", onClick = {}) }
}
