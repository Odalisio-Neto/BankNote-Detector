package com.neeto.banknotedetector.data

import android.content.Context
import android.graphics.Bitmap
import android.view.Surface
import android.view.SurfaceControl.TrustedPresentationThresholds
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class TFLiteClassifier(
    private val context : Context,
    private val threshold : Float = 0.7f,
    private val maxResults : Int = 1
) : Classifier {

    private var classifier: ImageClassifier? = null

    private fun setupClassifier(){
        val baseOptions = BaseOptions
            .builder()
            .setNumThreads(2)
            .useGpu()
            .build()
        val options = ImageClassifier
            .ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(maxResults)
            .setScoreThreshold(threshold)
            .build()

        try {
            classifier = ImageClassifier.createFromFileAndOptions(
                context,
                "best_model_cedulas_artigo.tflite",
                options
            )
        }catch (
            e : IllegalStateException
        ){
            e.printStackTrace()
        }
    }
    override fun classify(bitmap: Bitmap, rotation: Int): List<Classification> {
        if (classifier ==null){
            setupClassifier()
        }
        val imageProcessor = ImageProcessor.Builder().build()

        val image = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        val imageProcessingOptions = ImageProcessingOptions
            .builder()
            .setOrientation(getOrientation(rotation))
            .build()


        var results = classifier?.classify(image, imageProcessingOptions)

        return results?.flatMap {
            classifications ->
                classifications.categories.map {
                    category ->
                        Classification(
                            name = category.displayName,
                            score = category.score
                        )
                }
        }?.distinctBy { it.name } ?: emptyList()

    }

    fun getOrientation(rotation: Int) : ImageProcessingOptions.Orientation{
        return when(rotation){
            Surface.ROTATION_0 -> ImageProcessingOptions.Orientation.RIGHT_TOP
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
        }
    }
}