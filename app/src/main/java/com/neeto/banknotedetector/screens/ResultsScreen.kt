package com.neeto.banknotedetector.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neeto.banknotedetector.data.Classification
import com.neeto.banknotedetector.router.AppRouter
import com.neeto.banknotedetector.router.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    classifications : List<Classification>?
) {
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
            classifications?.forEach {
                Text(
                    text = it.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }



    }
}



