package com.neeto.banknotedetector.data

import android.graphics.Bitmap

interface Classifier {
    fun classify(bitmap: Bitmap, rotation : Int) : List <Classification>
}