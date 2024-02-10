package com.neeto.banknotedetector.screens


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neeto.banknotedetector.router.AppRouter
import com.neeto.banknotedetector.router.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.neeto.banknotedetector.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen() {
    val scope = rememberCoroutineScope()

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



