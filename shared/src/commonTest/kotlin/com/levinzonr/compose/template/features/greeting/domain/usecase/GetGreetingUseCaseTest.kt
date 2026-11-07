package com.levinzonr.compose.template.features.greeting.domain.usecase

import com.levinzonr.compose.template.features.greeting.domain.model.Greeting
import com.levinzonr.compose.template.features.greeting.fakes.FakeGreetingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetGreetingUseCaseTest {
    @Test
    fun givenRepositoryGreetingWhenInvokedThenReturnsGreeting() {
        // GIVEN
        val expected = Greeting(message = "Welcome!", platformName = "Test platform")
        val useCase = GetGreetingUseCase(FakeGreetingRepository(expected))

        // WHEN
        val actual = useCase()

        // THEN
        assertEquals(expected, actual)
    }
}
