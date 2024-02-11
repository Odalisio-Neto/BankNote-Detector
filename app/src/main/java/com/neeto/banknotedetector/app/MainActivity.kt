package com.neeto.banknotedetector.app

import androidx.compose.animation.Crossfade
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.neeto.banknotedetector.router.AppRouter
import com.neeto.banknotedetector.router.Screen
import com.neeto.banknotedetector.screens.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.os.Bundle
import android.util.Log
import androidx.compose.animation.core.tween
import com.neeto.banknotedetector.router.AppRouter.currentScreen

@Preview(showBackground = true, showSystemUi = true, name = "Main", group = "OneSync")
@Composable
fun BankNoteApp() {
    MaterialTheme {
        Crossfade(targetState = currentScreen, label = "" ,
            animationSpec = tween(durationMillis = 1000)
        ) {
            when(it.value){
                Screen.Home -> MainScreen()
                Screen.ResultsScreen -> ResultsScreen(null)
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


