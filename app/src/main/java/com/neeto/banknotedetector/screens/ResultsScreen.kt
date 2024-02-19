package com.neeto.banknotedetector.screens


import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.neeto.banknotedetector.data.Classification
import com.neeto.banknotedetector.router.AppRouter
import com.neeto.banknotedetector.router.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    listImagesResult : MutableList<Bitmap>?,
    classificationsList : MutableList<List<Classification>>?
) {
    Log.i("CLASSIFICATION", classificationsList.toString())

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
                Button(
                    onClick = { AppRouter.navigateTo(Screen.Home) }
                    ) {
                    Text(text = "Return to MAIN SCREEN")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items = listImagesResult ?: emptyList()) { index, image ->
                classificationsList?.getOrNull(index)?.let { classifications ->
                    ImageWithText(image = image, text = classifications)
                }
            }
        }



    }
}

@Composable
fun ImageWithText(image: Bitmap, text: List<Classification>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image on the left
        Image(
            bitmap = image.asImageBitmap(),
            contentDescription = null, // Provide a proper content description
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        // Spacer for some separation between image and text
        Spacer(modifier = Modifier.width(16.dp))

        // Text on the right
        Column {
            Text(text = "Detected note:")
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            text.forEach {
                Text(text = "Name: ${it.name}", fontWeight = FontWeight.Bold)
                Text(text = "Score: ${it.score}")
            }
        }
    }
}


