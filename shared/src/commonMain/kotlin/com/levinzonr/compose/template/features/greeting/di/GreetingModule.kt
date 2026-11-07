package com.levinzonr.compose.template.features.greeting.di

import org.koin.dsl.module

val greetingModule = module {
    includes(greetingDataModule, greetingDomainModule, greetingPresentationModule)
}
