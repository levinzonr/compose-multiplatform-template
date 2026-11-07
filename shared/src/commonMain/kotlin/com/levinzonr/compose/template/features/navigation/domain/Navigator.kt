package com.levinzonr.compose.template.features.navigation.domain

import com.levinzonr.compose.template.features.navigation.domain.model.Destination

/** One navigation API shared by all app Routes. */
interface Navigator {
    fun navigateTo(destination: Destination)
    fun goBack()
}
