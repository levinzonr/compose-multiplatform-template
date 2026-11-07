package com.levinzonr.compose.template.features.greeting.presentation.details

import androidx.lifecycle.ViewModel
import com.levinzonr.compose.template.features.greeting.domain.usecase.GetGreetingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class GreetingDetailsViewModel(getGreeting: GetGreetingUseCase) : ViewModel() {
    private val mutableState =
        MutableStateFlow(GreetingDetailsState(platformName = getGreeting().platformName))
    val state = mutableState.asStateFlow()
}
