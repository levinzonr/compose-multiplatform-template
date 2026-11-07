package com.levinzonr.compose.template.features.greeting.domain.usecase

import com.levinzonr.compose.template.features.greeting.domain.model.Greeting
import com.levinzonr.compose.template.features.greeting.domain.repository.GreetingRepository

class GetGreetingUseCase(private val repository: GreetingRepository) {
    operator fun invoke(): Greeting = repository.getGreeting()
}
