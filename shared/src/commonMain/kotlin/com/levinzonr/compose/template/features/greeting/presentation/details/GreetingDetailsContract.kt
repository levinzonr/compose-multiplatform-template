package com.levinzonr.compose.template.features.greeting.presentation.details

data class GreetingDetailsState(val platformName: String = "")

sealed interface GreetingDetailsAction {
    data object Back : GreetingDetailsAction
}
