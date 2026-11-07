package com.levinzonr.compose.template.features.greeting.presentation.greeting

import com.levinzonr.compose.template.features.greeting.domain.model.Greeting
import com.levinzonr.compose.template.features.greeting.domain.repository.GreetingRepository
import com.levinzonr.compose.template.features.greeting.domain.usecase.GetGreetingUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GreetingViewModelTest {
    @Test
    fun loadsGreetingAndTogglesContentWithoutLosingMessage() {
        val repository = object : GreetingRepository {
            override fun getGreeting(): Greeting = Greeting("Hello from fake!", "Fake OS")
        }
        val viewModel = GreetingViewModel(GetGreetingUseCase(repository))
        assertEquals("Hello from fake!", viewModel.state.value.message)
        assertFalse(viewModel.state.value.showContent)

        viewModel.toggleContent()
        assertTrue(viewModel.state.value.showContent)
        assertEquals("Hello from fake!", viewModel.state.value.message)

        viewModel.toggleContent()
        assertFalse(viewModel.state.value.showContent)
    }
}
