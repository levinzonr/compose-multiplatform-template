package com.levinzonr.compose.template.features.greeting.presentation.navigation

import com.levinzonr.compose.template.features.navigation.domain.model.Destination
import kotlinx.serialization.Serializable

@Serializable
data object GreetingDestination : Destination

@Serializable
data object GreetingDetailsDestination : Destination
