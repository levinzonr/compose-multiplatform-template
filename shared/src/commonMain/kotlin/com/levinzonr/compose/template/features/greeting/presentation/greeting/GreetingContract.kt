package com.levinzonr.compose.template.features.greeting.presentation.greeting

data class GreetingState(val message: String = "", val showContent: Boolean = false)

sealed interface GreetingAction {
    data object ToggleContent : GreetingAction
    data object OpenDetails : GreetingAction
}
