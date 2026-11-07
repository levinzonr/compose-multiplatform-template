package com.levinzonr.compose.template

import androidx.compose.runtime.Composable
import com.levinzonr.compose.template.app.di.appModules
import com.levinzonr.compose.template.app.navigation.AppNavigation
import com.levinzonr.compose.template.designsystem.theme.AppTheme
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

private val appConfiguration = koinConfiguration { modules(appModules) }

@Composable
fun App() {
    KoinApplication(configuration = appConfiguration) {
        AppTheme { AppNavigation() }
    }
}
