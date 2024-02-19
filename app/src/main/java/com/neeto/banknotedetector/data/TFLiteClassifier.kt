package com.neeto.banknotedetector.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.Surface
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class TFLiteClassifier(
    private val context : Context,
    private val threshold : Float = 0.1f,
    private val maxResults : Int = 5
) : Classifier {

    var classifier: ImageClassifier? = null

    fun setupClassifier(){
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
    fun getCategoryValue(index: Int): String {
        return when (index) {
            1 -> "2"
            2 -> "5"
            3 -> "10"
            4 -> "20"
            5 -> "50"
            else -> "0"
        }
    }
    override fun classify(bitmap: Bitmap, rotation: Int): List<Classification> {
        if (classifier == null){
            setupClassifier()
        }

        Log.i("CLASSIFIER", "getMaxResults : ${classifier}")
        val imageProcessor = ImageProcessor.Builder().build()

        val image = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        val imageProcessingOptions = ImageProcessingOptions
            .builder()
            .setOrientation(getOrientation(rotation))
            .build()


        val results = classifier?.classify(image, imageProcessingOptions)
        Log.i("CLASSIFIER", "results : ${results}")

        val list = results?.flatMap {
                classifications ->
            classifications.categories.map {
                    category ->
                Classification(
                    name = getCategoryValue(category.index),
                    score = category.score
                )
            }
        }?: emptyList()

        Log.i("CLASSIFIER", "results_list : ${list}")



        return list

    }

    private fun getOrientation(rotation: Int) : ImageProcessingOptions.Orientation{
        return when(rotation){
            Surface.ROTATION_0 -> ImageProcessingOptions.Orientation.RIGHT_TOP
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
        }
    }
}