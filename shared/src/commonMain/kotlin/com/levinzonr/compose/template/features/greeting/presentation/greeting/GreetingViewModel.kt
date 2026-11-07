package com.levinzonr.compose.template.features.greeting.presentation.greeting

import androidx.lifecycle.ViewModel
import com.levinzonr.compose.template.features.greeting.domain.usecase.GetGreetingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class GreetingViewModel(getGreeting: GetGreetingUseCase) : ViewModel() {
    private val mutableState = MutableStateFlow(GreetingState(message = getGreeting().message))
    val state = mutableState.asStateFlow()

    fun toggleContent() {
        mutableState.update { it.copy(showContent = !it.showContent) }
    }
}
