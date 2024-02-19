    @file:OptIn(ExperimentalMaterial3Api::class)

    package com.neeto.banknotedetector.screens



    import CameraUsage
    import android.graphics.Bitmap
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.BottomSheetScaffold
    import androidx.compose.material3.BottomSheetScaffoldState
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.SnackbarHostState
    import androidx.compose.material3.Tab
    import androidx.compose.material3.TabRow
    import androidx.compose.material3.Text
    import androidx.compose.material3.TopAppBar
    import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
    import androidx.compose.material3.rememberBottomSheetScaffoldState
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableIntStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.google.accompanist.permissions.ExperimentalPermissionsApi
    import com.neeto.banknotedetector.app.MainViewModel
    import com.neeto.banknotedetector.components.PhotoSheetContent
    import com.neeto.banknotedetector.data.Classification
    import com.neeto.banknotedetector.data.OpenLinkInBrowser
    import kotlinx.coroutines.CoroutineScope

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
    @Composable
    fun MainScreen(
        listImagesResult: MutableList<Bitmap>,
        listOfClassifications: MutableList<List<Classification>>,
    ) {

        val scope  = rememberCoroutineScope()

        val snackbarHostState = remember {
            SnackbarHostState()
        }

        val scaffoldState = rememberBottomSheetScaffoldState(snackbarHostState = snackbarHostState)


        val viewModel = viewModel<MainViewModel>()
        val bitmaps by viewModel.bitmaps.collectAsState()

        val context = LocalContext.current




        BottomSheetScaffold(
            scaffoldState = scaffoldState,

            sheetContent = {
                /*
                * Sheet content for galery images
                * */
                PhotoSheetContent(
                    bitmaps,
                    modifier = Modifier.fillMaxWidth(),
                    updateImagesResult = {
                        listImagesResult.add(it)
                    },
                    updateClassifications = {
                        listOfClassifications.add(it)
                    }
                )

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
                context.display?.rotation?.let { it1 ->
                    TabScreen(it, scope, scaffoldState,
                        it1
                    )
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




