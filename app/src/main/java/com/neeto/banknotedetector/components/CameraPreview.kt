package com.neeto.banknotedetector.components

import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner

@Composable

fun CameraPreview(
    lifecycleOwner : LifecycleOwner,
    controller: LifecycleCameraController,
    modifier: Modifier
) {

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
