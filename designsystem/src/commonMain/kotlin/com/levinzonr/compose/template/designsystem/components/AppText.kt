package com.levinzonr.compose.template.designsystem.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.levinzonr.compose.template.designsystem.theme.AppTheme

/** Shared body text; callers select other semantic typography/color roles through AppTheme. */
@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = AppTheme.typography.bodyLarge,
    color: Color = AppTheme.colors.onSurface
) {
    Text(text = text, modifier = modifier, style = style, color = color)
}

@Preview
@Composable
private fun AppTextPreview() {
    AppTheme(darkTheme = false) { AppText(text = "Shared body text") }
}

@Preview
@Composable
private fun AppTextDarkPreview() {
    AppTheme(darkTheme = true) { AppText(text = "Shared body text") }
}
