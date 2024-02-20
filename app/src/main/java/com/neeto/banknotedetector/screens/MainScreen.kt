    @file:OptIn(ExperimentalMaterial3Api::class)

    package com.neeto.banknotedetector.screens



    import CameraUsage
    import android.graphics.Bitmap
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.aspectRatio
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.heightIn
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
    import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
    import androidx.compose.foundation.lazy.staggeredgrid.items
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
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.google.accompanist.permissions.ExperimentalPermissionsApi
    import com.neeto.banknotedetector.R
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
                    // Conteúdo da Aba 1
                    CameraUsage(paddingValues,
                        scope,
                        scaffoldState,
                        rotation
                    )
                }
                1 -> {
                    // Conteúdo da Aba 2
                    SobreAplicativo(
                        paddingValues
                    )
                }
            }
        }
    }

    @Composable
    private fun SobreAplicativo(
        paddingValues: PaddingValues
    ) {
        val autores = listOf("Odalisio L. S. Neto(1)","Felipe G. Oliveira(2)", "João M. B. Cavalcanti(1)", "José L. S. Pio(1)")
        val notas = listOf<Int>(R.drawable.note1, R.drawable.note2, R.drawable.note3, R.drawable.note4)
        
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Aplicativo organizado como resultado do artigo de detecção de notas bancárias.",
                    textAlign = TextAlign.Justify,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Odalisio L. S. Neto(1),Felipe G. Oliveira(2), João M. B. Cavalcanti(1), José L. S. Pio(1).",
                    textAlign = TextAlign.Justify,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(10.dp)
                )

                Text(
                    text = "(1) - Instituto de Computação - UFAM, (2) - Instituto de Ciências Exatas e Tecnologia - UFAM",
                    fontSize = 10.sp,
                    modifier = Modifier.padding(10.dp)
                )
                OpenLinkInBrowser()

            }

            item {
                Text(text = "As imagens abaixo mostram alguns exemplos das imagens esperadas",
                    fontSize = 14.sp,
                    )

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalItemSpacing = 2.dp,
                    contentPadding = PaddingValues(2.dp),
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(notas) { item ->
                        Image(
                            painter = painterResource(id = item),
                            contentDescription = "Example Image",
                            modifier = Modifier.aspectRatio(1f)
                        )
                    }
                }
            }




            item { Text(text = "Oferecimento :", fontSize = 14.sp,
                modifier = Modifier.padding(5.dp)
                ) }
            item {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    Image(painter = painterResource(id = R.drawable.moto_logo),
                        modifier = Modifier
                            .padding(15.dp)
                            .size(150.dp),
                        contentDescription = null)
                    Image(painter = painterResource(id = R.drawable.impact_logo),
                        modifier = Modifier
                            .padding(15.dp)
                            .size(150.dp),
                        contentDescription = null)
                }

            }

        }



//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .verticalScroll(
//                    state = rememberScrollState()
//                    )
//        ) {
//            Text(
//                text = "Aplicativo organizado como resultado do artigo de detecção de notas bancárias.",
//                textAlign = TextAlign.Justify,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            Image(
//                painter = painterResource(id = R.drawable.smashedbanknote),
//                modifier = Modifier.fillMaxWidth(),
//                contentDescription = "Smashed Banknote"
//            )
//
//            Text(text = "As imagens abaixo mostram alguns exemplos das imagens esperadas")
//            val items = listOf<Int>(R.drawable.note1, R.drawable.note2, R.drawable.note3, R.drawable.note4)
//
//            LazyVerticalStaggeredGrid(
//                columns = StaggeredGridCells.Fixed(2),
//                horizontalArrangement = Arrangement.spacedBy(20.dp),
//                verticalItemSpacing = 15.dp,
//                contentPadding = PaddingValues(15.dp),
////                modifier = modifier
//            ) {
//                items(items) { item ->
//                    Image(
//                        painter = painterResource(id = item),
//                        contentDescription = "Example Image"
//                    )
//                }
//            }
//
//            Text(text = "Acesso :")
//            OpenLinkInBrowser()
//
//            Text(
//                text = "Oferecimento :"
//            )
//            Column(
//                modifier = Modifier
////                    .fillMaxWidth()
//                    .padding(15.dp)
//            ) {
//                Image(painter = painterResource(id = R.drawable.moto_logo), contentDescription = null)
//                Image(painter = painterResource(id = R.drawable.impact_logo), contentDescription = null)
//
//            }
//        }
    }
