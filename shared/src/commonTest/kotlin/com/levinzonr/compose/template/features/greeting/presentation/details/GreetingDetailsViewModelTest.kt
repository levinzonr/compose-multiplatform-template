package com.levinzonr.compose.template.features.greeting.presentation.details

import com.levinzonr.compose.template.features.greeting.domain.model.Greeting
import com.levinzonr.compose.template.features.greeting.domain.usecase.GetGreetingUseCase
import com.levinzonr.compose.template.features.greeting.fakes.FakeGreetingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GreetingDetailsViewModelTest {
    @Test
    fun givenPlatformGreetingWhenCreatedThenDisplaysPlatform() {
        // GIVEN
        val repository = FakeGreetingRepository(Greeting("Hello!", "Fake iOS"))

        // WHEN
        val viewModel = GreetingDetailsViewModel(GetGreetingUseCase(repository))

        // THEN
        assertEquals(GreetingDetailsState(platformName = "Fake iOS"), viewModel.state.value)
    }
}
