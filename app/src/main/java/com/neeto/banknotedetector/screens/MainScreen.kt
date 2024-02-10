@file:OptIn(ExperimentalMaterial3Api::class)

package com.neeto.banknotedetector.screens



import CameraUsage
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neeto.banknotedetector.router.AppRouter
import com.neeto.banknotedetector.router.Screen
import kotlinx.coroutines.CoroutineScope
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.neeto.banknotedetector.app.MainViewModel
import com.neeto.banknotedetector.components.LockVolumeButtons
import com.neeto.banknotedetector.components.OpenLinkInBrowser
import com.neeto.banknotedetector.components.PhotoSheetContent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val scope  = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    LockVolumeButtons()

    val viewModel = viewModel<MainViewModel>()
    val bitmaps by viewModel.bitmaps.collectAsState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            /*
            * Sheet content for galery images
            * */
            PhotoSheetContent(
                bitmaps,
                modifier = Modifier.fillMaxWidth()
            )
        },
        sheetPeekHeight = 0.dp,
        topBar = {
            /*
            * Top bar for showing the name of the app
            * */

            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "BankNote Detector",
                        modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                )

            },

        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                ,
        ) {
            when (AppRouter.currentScreen.value) {
                Screen.Home -> {
                    TabScreen(it, scope, scaffoldState)
                }
                Screen.ResultsScreen -> {
                    ResultsScreen()
                }
            }
        }
    }


}


@Composable
fun TabScreen(paddingValues: PaddingValues,
              scope: CoroutineScope,
              scaffoldState : BottomSheetScaffoldState
) {
    val tabs = listOf("Camera", "Sobre o aplicativo")
    var selectedTabIndex by remember{ mutableIntStateOf(0) }

    Column {
        TabRow(selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> {

                CameraUsage(paddingValues,
                    scope,
                    scaffoldState
                )
            }
            1 -> {
                // Conteúdo da Aba 2
                Text(
                    text = """
                        Aplicativo organizado como resultado do artigo de detecção de notas bancárias.
                        Acesso :

                    """.trimIndent(),
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OpenLinkInBrowser()
            }
        }
    }
}




