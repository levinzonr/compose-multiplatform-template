package com.levinzonr.compose.template.features.navigation.framework

import androidx.navigation3.runtime.NavKey
import com.levinzonr.compose.template.features.navigation.domain.model.Destination
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/** Keeps Navigation 3 and its serialization details outside the destination contract. */
@Serializable
internal data class NavigationEntry(@Polymorphic val destination: Destination) : NavKey
