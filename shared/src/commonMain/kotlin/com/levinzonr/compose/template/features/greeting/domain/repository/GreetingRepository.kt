package com.levinzonr.compose.template.features.greeting.domain.repository

import com.levinzonr.compose.template.features.greeting.domain.model.Greeting

interface GreetingRepository {
    fun getGreeting(): Greeting
}
