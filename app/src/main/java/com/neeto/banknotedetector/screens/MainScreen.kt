@file:OptIn(ExperimentalMaterial3Api::class)

package com.neeto.banknotedetector.screens



import CameraUsage
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neeto.banknotedetector.router.AppRouter
import com.neeto.banknotedetector.router.Screen
import kotlinx.coroutines.CoroutineScope
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.neeto.banknotedetector.app.MainViewModel
import com.neeto.banknotedetector.data.LockVolumeButtons
import com.neeto.banknotedetector.data.OpenLinkInBrowser
import com.neeto.banknotedetector.components.PhotoSheetContent
import com.neeto.banknotedetector.data.Classification
import com.neeto.banknotedetector.data.ImageAnalyser
import com.neeto.banknotedetector.data.TFLiteClassifier

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {

    val scope  = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val isPortrait = LocalContext.current
        .resources
        .configuration
        .orientation == Configuration.ORIENTATION_PORTRAIT


    val viewModel = viewModel<MainViewModel>()
    val bitmaps by viewModel.bitmaps.collectAsState()

    val context = LocalContext.current

    var classifications by remember {
        mutableStateOf(emptyList<Classification>())
    }
    val classifier = TFLiteClassifier (context = context)


    BottomSheetScaffold(
        scaffoldState = scaffoldState,

        sheetContent = {
            /*
            * Sheet content for galery images
            * */
            PhotoSheetContent(
                bitmaps,
                modifier = Modifier.fillMaxWidth(),
                classifier = classifier,
            ){
                newClassifications ->
                classifications = newClassifications
            }
            Log.i("CLASSIFICATION : ", classifications.toString())

        },
        sheetPeekHeight = 0.dp,
        topBar = {
            /*
            * Top bar for showing the name of the app
            * */
                // Portrait
                if (context.display?.rotation == android.view.Surface.ROTATION_0) {
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
                } else {
                    Spacer(modifier = Modifier.height(0.dp) )// Or any empty placeholder
                }

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
                    context.display?.rotation?.let { it1 ->
                        TabScreen(it, scope, scaffoldState,
                            it1
                        )
                    }
                }
                Screen.ResultsScreen -> {
                    ResultsScreen( classifications )
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabScreen(paddingValues: PaddingValues,
              scope: CoroutineScope,
              scaffoldState : BottomSheetScaffoldState,
              rotation : Int
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
                    scaffoldState,
                    rotation
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




