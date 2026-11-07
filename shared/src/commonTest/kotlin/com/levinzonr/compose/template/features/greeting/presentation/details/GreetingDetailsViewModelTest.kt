package com.levinzonr.compose.template.features.greeting.presentation.details

import com.levinzonr.compose.template.features.greeting.domain.model.Greeting
import com.levinzonr.compose.template.features.greeting.domain.repository.GreetingRepository
import com.levinzonr.compose.template.features.greeting.domain.usecase.GetGreetingUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class GreetingDetailsViewModelTest {
    @Test
    fun displaysPlatformFromDomain() {
        val repository = object : GreetingRepository {
            override fun getGreeting(): Greeting = Greeting("Hello!", "Fake iOS")
        }
        val viewModel = GreetingDetailsViewModel(GetGreetingUseCase(repository))
        assertEquals(GreetingDetailsState(platformName = "Fake iOS"), viewModel.state.value)
    }
}
