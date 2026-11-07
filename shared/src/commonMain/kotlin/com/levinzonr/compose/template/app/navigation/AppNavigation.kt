package com.levinzonr.compose.template.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.levinzonr.compose.template.features.greeting.presentation.details.GreetingDetailsRoute
import com.levinzonr.compose.template.features.greeting.presentation.greeting.GreetingRoute
import com.levinzonr.compose.template.features.greeting.presentation.navigation.GreetingDestination
import com.levinzonr.compose.template.features.greeting.presentation.navigation.GreetingDetailsDestination
import com.levinzonr.compose.template.features.navigation.domain.model.Destination
import com.levinzonr.compose.template.features.navigation.framework.NavigationEntry
import com.levinzonr.compose.template.features.navigation.framework.Navigator3
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val navigationConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(NavigationEntry::class, NavigationEntry.serializer())
        }
        polymorphic(Destination::class) {
            subclass(GreetingDestination::class, GreetingDestination.serializer())
            subclass(GreetingDetailsDestination::class, GreetingDetailsDestination.serializer())
        }
    }
}

@Composable
internal fun AppNavigation() {
    val backStack =
        rememberNavBackStack(navigationConfiguration, NavigationEntry(GreetingDestination))
    val navigator = remember(backStack) { Navigator3(backStack) }
    NavDisplay(
        backStack = backStack,
        onBack = navigator::goBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavigationEntry> { entry ->
                when (val destination = entry.destination) {
                    GreetingDestination -> GreetingRoute(navigator)
                    GreetingDetailsDestination -> GreetingDetailsRoute(navigator)
                    else -> error("Unregistered destination: $destination")
                }
            }
        }
    )
}
