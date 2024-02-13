package com.neeto.banknotedetector.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.neeto.banknotedetector.screens.MainScreen

@Preview(showBackground = true, showSystemUi = true, name = "Main", group = "OneSync")
@Composable
fun BankNoteApp() {
    MaterialTheme {
        MainScreen()
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


