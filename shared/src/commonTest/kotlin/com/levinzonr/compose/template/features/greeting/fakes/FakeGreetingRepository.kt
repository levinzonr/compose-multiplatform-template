package com.levinzonr.compose.template.features.greeting.fakes

import com.levinzonr.compose.template.features.greeting.domain.model.Greeting
import com.levinzonr.compose.template.features.greeting.domain.repository.GreetingRepository

internal class FakeGreetingRepository(
    var result: Greeting = Greeting("Hello from fake!", "Fake OS")
) : GreetingRepository {
    override fun getGreeting(): Greeting = result
}
