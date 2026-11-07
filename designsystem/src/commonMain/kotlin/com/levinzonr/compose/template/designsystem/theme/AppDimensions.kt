package com.levinzonr.compose.template.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Layout and component sizing roles; feature code consumes these instead of raw dp values. */
@Immutable
data class AppDimensions(
    val componentGap: Dp = 8.dp,
    val contentGap: Dp = 16.dp,
    val sectionGap: Dp = 24.dp,
    val screenPadding: Dp = 24.dp,
    val buttonMinHeight: Dp = 48.dp,
    val buttonHorizontalPadding: Dp = 24.dp,
    val buttonVerticalPadding: Dp = 12.dp
)
