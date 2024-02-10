package com.neeto.banknotedetector.router
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Class defining the screens we have in the app.
 *
 * These objects should match files we have in the screens package
 */
sealed class Screen(val title: String) {
    object Home : Screen("Home")
    object ResultsScreen : Screen("Settings")
}




object AppRouter {
    var currentScreen : MutableState<Screen> = mutableStateOf(Screen.Home)

    fun navigateTo(destination: Screen) {
        currentScreen.value = destination
    }
}