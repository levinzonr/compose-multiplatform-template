package com.levinzonr.compose.template.features.greeting.di

import com.levinzonr.compose.template.features.greeting.domain.usecase.GetGreetingUseCase
import org.koin.dsl.module

internal val greetingDomainModule = module {
    factory { GetGreetingUseCase(get()) }
}
