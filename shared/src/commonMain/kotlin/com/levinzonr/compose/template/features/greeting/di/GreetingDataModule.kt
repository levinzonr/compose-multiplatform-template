package com.levinzonr.compose.template.features.greeting.di

import com.levinzonr.compose.template.features.greeting.data.datasource.PlatformDataSource
import com.levinzonr.compose.template.features.greeting.data.repository.DefaultGreetingRepository
import com.levinzonr.compose.template.features.greeting.domain.repository.GreetingRepository
import com.levinzonr.compose.template.features.greeting.framework.NativePlatformDataSource
import org.koin.dsl.module

internal val greetingDataModule = module {
    single<PlatformDataSource> { NativePlatformDataSource() }
    single<GreetingRepository> { DefaultGreetingRepository(get()) }
}
