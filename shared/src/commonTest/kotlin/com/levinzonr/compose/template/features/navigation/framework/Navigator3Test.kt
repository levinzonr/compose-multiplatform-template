package com.levinzonr.compose.template.features.navigation.framework

import androidx.navigation3.runtime.NavKey
import com.levinzonr.compose.template.features.navigation.domain.model.Destination
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class Navigator3Test {
    @Test
    fun pushesDestinationAndReturnsToPreviousEntry() {
        val root = NavigationEntry(Home)
        val backStack = mutableListOf<NavKey>(root)
        val navigator = Navigator3(backStack)

        navigator.navigateTo(Details("42"))
        assertEquals(listOf<NavKey>(root, NavigationEntry(Details("42"))), backStack)

        navigator.goBack()
        assertEquals(listOf<NavKey>(root), backStack)
    }

    @Test
    fun preventsDuplicateTopButAllowsDifferentArguments() {
        val backStack = mutableListOf<NavKey>(NavigationEntry(Home))
        val navigator = Navigator3(backStack)

        navigator.navigateTo(Details("42"))
        navigator.navigateTo(Details("42"))
        assertEquals(2, backStack.size)

        navigator.navigateTo(Details("43"))
        assertEquals(3, backStack.size)
        assertEquals(NavigationEntry(Details("43")), backStack.last())
    }

    @Test
    fun neverPopsRoot() {
        val root = NavigationEntry(Home)
        val backStack = mutableListOf<NavKey>(root)
        Navigator3(backStack).goBack()
        assertEquals(listOf<NavKey>(root), backStack)
    }

    @Test
    fun roundTripsDestinationArgumentsWithExplicitSerializers() {
        val json = Json {
            serializersModule = SerializersModule {
                polymorphic(Destination::class) {
                    subclass(Home::class, Home.serializer())
                    subclass(Details::class, Details.serializer())
                }
            }
        }
        val entry = NavigationEntry(Details("42"))
        val encoded = json.encodeToString(NavigationEntry.serializer(), entry)
        assertEquals(entry, json.decodeFromString(NavigationEntry.serializer(), encoded))
    }
}

@Serializable
private data object Home : Destination

@Serializable
private data class Details(val id: String) : Destination
