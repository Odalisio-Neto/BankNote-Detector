package com.neeto.banknotedetector.app

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.neeto.banknotedetector.data.Classification
import com.neeto.banknotedetector.router.AppRouter
import com.neeto.banknotedetector.router.Screen
import com.neeto.banknotedetector.screens.MainScreen
import com.neeto.banknotedetector.screens.ResultsScreen

@Preview(showBackground = true, showSystemUi = true, name = "Main", group = "OneSync")
@Composable
fun BankNoteApp() {
    var listImagesResult = mutableListOf<Bitmap>()
    var listOfClassifications = mutableListOf<List<Classification>>()

    MaterialTheme {
        Crossfade(
            targetState = AppRouter.currentScreen,
            animationSpec = tween(durationMillis = 2000),
            label = ""
        ) { screen ->
            when (screen.value) {
                is Screen.Home -> MainScreen(listImagesResult, listOfClassifications)
                is Screen.ResultsScreen -> ResultsScreen(listImagesResult, listOfClassifications)
            }
        }
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BankNoteApp()
        }
    }
}


