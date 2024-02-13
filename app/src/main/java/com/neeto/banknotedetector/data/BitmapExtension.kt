package com.neeto.banknotedetector.data

import android.graphics.Bitmap
import android.graphics.Matrix

fun Bitmap.centercrop(
    desiredWidth: Int,
    desiredHeight: Int
) : Bitmap{
    val xStart = (width - desiredWidth) / 2
    val yStart = (height - desiredHeight) / 2

    if( xStart < 0 || yStart < 0 || desiredWidth > width || desiredHeight > height)
        throw IllegalArgumentException("Invalid arguments for center cropping")

    return Bitmap.createBitmap(this, xStart, yStart, desiredWidth, desiredHeight)
}
fun Bitmap.resize(
    desiredWidth: Int,
    desiredHeight: Int
): Bitmap {
    if (desiredWidth <= 0 || desiredHeight <= 0) {
        throw IllegalArgumentException("Invalid arguments for resizing")
    }

    val originalWidth = width
    val originalHeight = height

    val scaleWidth = desiredWidth.toFloat() / originalWidth
    val scaleHeight = desiredHeight.toFloat() / originalHeight

    // Use the maximum scaling factor to maintain the aspect ratio
    val scaleFactor = maxOf(scaleWidth, scaleHeight)

    val newWidth = (originalWidth * scaleFactor).toInt()
    val newHeight = (originalHeight * scaleFactor).toInt()

    return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
}

fun Bitmap.force_resize(
    desiredWidth: Int,
    desiredHeight: Int
): Bitmap {
    if (desiredWidth <= 0 || desiredHeight <= 0) {
        throw IllegalArgumentException("Invalid arguments for resizing")
    }

    return Bitmap.createScaledBitmap(this, desiredWidth, desiredHeight, true)
}
fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}