package com.levinzonr.compose.template.features.greeting.presentation.greeting

import com.levinzonr.compose.template.features.greeting.domain.usecase.GetGreetingUseCase
import com.levinzonr.compose.template.features.greeting.fakes.FakeGreetingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GreetingViewModelTest {
    @Test
    fun givenGreetingWhenCreatedThenContentIsCollapsed() {
        // GIVEN
        val repository = FakeGreetingRepository()

        // WHEN
        val viewModel = GreetingViewModel(GetGreetingUseCase(repository))

        // THEN
        assertEquals(
            GreetingState(message = repository.result.message, showContent = false),
            viewModel.state.value
        )
    }

    @Test
    fun givenCollapsedContentWhenToggledThenExpandsAndPreservesMessage() {
        // GIVEN
        val repository = FakeGreetingRepository()
        val viewModel = GreetingViewModel(GetGreetingUseCase(repository))

        // WHEN
        viewModel.toggleContent()

        // THEN
        assertEquals(
            GreetingState(message = repository.result.message, showContent = true),
            viewModel.state.value
        )
    }

    @Test
    fun givenExpandedContentWhenToggledThenCollapsesAndPreservesMessage() {
        // GIVEN
        val repository = FakeGreetingRepository()
        val viewModel = GreetingViewModel(GetGreetingUseCase(repository))
        viewModel.toggleContent()

        // WHEN
        viewModel.toggleContent()

        // THEN
        assertEquals(
            GreetingState(message = repository.result.message, showContent = false),
            viewModel.state.value
        )
    }
}
