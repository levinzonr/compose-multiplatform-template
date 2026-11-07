package com.levinzonr.compose.template.features.greeting.presentation.greeting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levinzonr.compose.template.features.greeting.presentation.navigation.GreetingDetailsDestination
import com.levinzonr.compose.template.features.navigation.domain.Navigator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GreetingRoute(navigator: Navigator) {
    val viewModel = koinViewModel<GreetingViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    GreetingScreen(state = state, onAction = { action ->
        when (action) {
            GreetingAction.ToggleContent -> viewModel.toggleContent()
            GreetingAction.OpenDetails -> navigator.navigateTo(GreetingDetailsDestination)
        }
    })
}
