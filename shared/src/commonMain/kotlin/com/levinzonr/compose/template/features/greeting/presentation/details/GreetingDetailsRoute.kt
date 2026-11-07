package com.levinzonr.compose.template.features.greeting.presentation.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levinzonr.compose.template.features.navigation.domain.Navigator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GreetingDetailsRoute(navigator: Navigator) {
    val viewModel = koinViewModel<GreetingDetailsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    GreetingDetailsScreen(state = state, onAction = { action ->
        when (action) {
            GreetingDetailsAction.Back -> navigator.goBack()
        }
    })
}
