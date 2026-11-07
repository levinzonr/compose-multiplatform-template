package com.levinzonr.compose.template.features.greeting.data.repository

import com.levinzonr.compose.template.features.greeting.data.datasource.PlatformDataSource
import com.levinzonr.compose.template.features.greeting.domain.model.Greeting
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultGreetingRepositoryTest {
    @Test
    fun mapsPlatformNameIntoGreeting() {
        val source = object : PlatformDataSource {
            override fun getPlatformName(): String = "Test OS"
        }
        val repository = DefaultGreetingRepository(source)
        assertEquals(
            Greeting(message = "Hello, Test OS!", platformName = "Test OS"),
            repository.getGreeting()
        )
    }
}
