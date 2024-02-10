package com.neeto.banknotedetector.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.neeto.banknotedetector.router.AppRouter
import com.neeto.banknotedetector.router.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen() {
    rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "BankNote Detector - Results ",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )

        },
        bottomBar = {
            BottomAppBar (
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()

            ){
                Button(onClick = { AppRouter.navigateTo(Screen.Home) }) {
                    Text(text = "Return to MAIN SCREEN")
                }
            }
        }
    ) { innerPadding ->
        Column (modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ){
            Text("Results 1")
            Text("Results 2")
            Text("Results 3")
            Text("Results 4")
            Text("Results 5")
            Text("Results 6")
            Text("Results 7")
            Text("Results 8")
        }



    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewResults()
{
    ResultsScreen()
}



