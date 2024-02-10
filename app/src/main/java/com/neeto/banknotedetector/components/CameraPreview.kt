package com.neeto.banknotedetector.components

import android.util.Log
import android.view.KeyEvent
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

@Composable

fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier
){
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            val previewView: PreviewView = PreviewView(it).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            }

            previewView.controller = controller
            controller.bindToLifecycle(lifecycleOwner)
            previewView
        },
        modifier = modifier
    )


}