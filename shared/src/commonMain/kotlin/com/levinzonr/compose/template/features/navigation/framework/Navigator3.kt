package com.levinzonr.compose.template.features.navigation.framework

import androidx.navigation3.runtime.NavKey
import com.levinzonr.compose.template.features.navigation.domain.Navigator
import com.levinzonr.compose.template.features.navigation.domain.model.Destination

/** Created once by the app host against its saveable Navigation 3 back stack. */
internal class Navigator3(private val backStack: MutableList<NavKey>) : Navigator {
    override fun navigateTo(destination: Destination) {
        val entry = NavigationEntry(destination)
        if (backStack.lastOrNull() != entry) backStack.add(entry)
    }

    override fun goBack() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }
}
