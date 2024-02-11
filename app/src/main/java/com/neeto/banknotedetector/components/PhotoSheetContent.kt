package com.neeto.banknotedetector.components

import android.graphics.Bitmap
import android.util.Log
import android.view.Surface
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.neeto.banknotedetector.R
import com.neeto.banknotedetector.data.Classification
import com.neeto.banknotedetector.data.TFLiteClassifier
import com.neeto.banknotedetector.data.centercrop

@Composable
fun PhotoSheetContent(
    images: List<Bitmap>,
    modifier: Modifier,
    onConfirmation: (bitmap: Bitmap) -> Unit = {},
    classifier: TFLiteClassifier,
    updateClassifications: (List<Classification>) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current


    if (images.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            EmptyStateImage()
            Text(text = "No photos yet", style = TextStyle.Default)
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalItemSpacing = 15.dp,
            contentPadding = PaddingValues(15.dp),
            modifier = modifier
        ) {
            items(images) { bitmap ->
                PhotoItem(bitmap) {
                    selectedBitmap = bitmap
                    showDialog = true
                }
            }
        }
    }
    Log.i("CLASSIFICATION : ", "ROTATION : ${context.display?.rotation}")
    if (showDialog) {
        PhotoConfirmationDialog(
            showDialog = showDialog,
            onConfirm = {
                showDialog = false
                onConfirmation(selectedBitmap!!) // Ensure selectedBitmap is not null
                updateClassifications(classifier.classify(
                    selectedBitmap!!.centercrop(desiredHeight = 128, desiredWidth = 154),
                    rotation = context.display?.rotation ?: Surface.ROTATION_0
                ))
            },
            onDismiss = {
                showDialog = false
                selectedBitmap = null // Clear selected bitmap on dismiss
            }
        )
    }
}

@Composable
private fun PhotoItem(
    bitmap: Bitmap,
    onConfirmation: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable(onClick = onConfirmation)
    ) {
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
        IconButton(
            onClick = onConfirmation,
            modifier = Modifier.fillMaxSize()
        ){}
    }
}

@Composable
private fun EmptyStateImage() {
    Image(
        painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "No photos yet",
        modifier = Modifier.size(128.dp)
    )
}

@Composable
private fun PhotoConfirmationDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Image Processing") },
            text = { Text("Proceed to process the image?") },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Dismiss")
                }
            }
        )
    }
}
