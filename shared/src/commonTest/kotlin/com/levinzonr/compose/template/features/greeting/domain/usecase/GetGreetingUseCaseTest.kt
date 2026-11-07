package com.levinzonr.compose.template.features.greeting.domain.usecase

import com.levinzonr.compose.template.features.greeting.domain.model.Greeting
import com.levinzonr.compose.template.features.greeting.domain.repository.GreetingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetGreetingUseCaseTest {
    @Test
    fun returnsGreetingProvidedByRepository() {
        val expected = Greeting(message = "Welcome!", platformName = "Test platform")
        val repository = object : GreetingRepository {
            override fun getGreeting(): Greeting = expected
        }
        assertEquals(expected, GetGreetingUseCase(repository)())
    }
}
