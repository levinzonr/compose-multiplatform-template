package com.levinzonr.compose.template.features.greeting.data.repository

import com.levinzonr.compose.template.features.greeting.data.datasource.PlatformDataSource
import com.levinzonr.compose.template.features.greeting.domain.model.Greeting
import com.levinzonr.compose.template.features.greeting.domain.repository.GreetingRepository

internal class DefaultGreetingRepository(private val platformDataSource: PlatformDataSource) :
    GreetingRepository {
    override fun getGreeting(): Greeting {
        val platformName = platformDataSource.getPlatformName()
        return Greeting(message = "Hello, $platformName!", platformName = platformName)
    }
}
