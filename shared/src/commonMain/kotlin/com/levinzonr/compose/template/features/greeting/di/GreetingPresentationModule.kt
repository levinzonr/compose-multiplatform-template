package com.levinzonr.compose.template.features.greeting.di

import com.levinzonr.compose.template.features.greeting.presentation.details.GreetingDetailsViewModel
import com.levinzonr.compose.template.features.greeting.presentation.greeting.GreetingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val greetingPresentationModule = module {
    viewModelOf(::GreetingViewModel)
    viewModelOf(::GreetingDetailsViewModel)
}
