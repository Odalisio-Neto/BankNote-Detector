package com.neeto.banknotedetector.data

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class ImageAnalyser(
    val classifier : TFLiteClassifier,
    val onResult : (List<Classification>) -> Unit
) : ImageAnalysis.Analyzer
{

    override fun analyze(image: ImageProxy) {
        val rotationDegreesValue = image.imageInfo.rotationDegrees
        val bitmap = image
            .toBitmap()
            .centercrop(desiredHeight = 128, desiredWidth = 154) //from pretrained model (Xtrain.shape)

        val results = classifier.classify(bitmap, rotationDegreesValue)
        onResult(results)

        image.close()
    }
}