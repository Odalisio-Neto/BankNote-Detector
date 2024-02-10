//import com.google.accompanist.permissions.rememberPermissionState
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.Icon
import android.graphics.drawable.ShapeDrawable
import android.media.AudioAttributes
import android.media.AudioManager
import android.provider.MediaStore
import android.service.autofill.OnClickAction
import android.util.Log
import android.view.KeyEvent
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.utils.MatrixExt.postRotate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.neeto.banknotedetector.R
import com.neeto.banknotedetector.components.CameraPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.material3.Icon
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neeto.banknotedetector.app.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable

fun CameraUsage(
    paddingValues: PaddingValues,
    scope : CoroutineScope,
    scaffoldState : BottomSheetScaffoldState
) {
    val viewModel = viewModel<MainViewModel>()
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    if (cameraPermissionState.status.isGranted) {

        Box(modifier = Modifier
            .padding(paddingValues)
        ) {
            val contextCamera = LocalContext.current
            val cameraController = remember{ LifecycleCameraController(contextCamera)
                .apply { setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE
                )
                }
            }
            CameraPreview(
                controller = cameraController,
                modifier = Modifier.fillMaxSize()
            )

            Row (
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ){
                CameraIcons(
                    drawableId = R.drawable.photo_library,
                    description = "Open Gallery",
                    onclick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    }
                )

                val cameraContext = ContextCompat.getMainExecutor(LocalContext.current)

                Canvas(modifier = Modifier
                    .size(100.dp)
                    .clickable(onClick = {

                        cameraController.takePicture(
                            cameraContext,
                            object : OnImageCapturedCallback() {
                                override fun onCaptureSuccess(image: ImageProxy) {
                                    super.onCaptureSuccess(image)

                                    val matrix = Matrix().apply {
                                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                                    }
                                    val rotatedBitmap = Bitmap.createBitmap(
                                        image.toBitmap(),
                                        0,
                                        0,
                                        image.width,
                                        image.height,
                                        matrix,
                                        true
                                    )

                                    (viewModel::onTakePhoto)(rotatedBitmap)
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    super.onError(exception)
                                    Log.e("Camera", "Couldn't take photo: ", exception)
                                }
                            }
                        )
                    })) {
                    val radius = size.minDimension / 2
                    val strokeWidth = 10.dp.toPx()

                    // Draw the circle
                    drawCircle(
                        color = Color.White,
                        radius = radius,
                        center = Offset(size.width / 2, size.height / 2)
                    )

                    // Draw the border
                    drawCircle(
                        color = Color.LightGray,
                        style = Stroke(width = strokeWidth),
                        radius = radius - strokeWidth / 2,
                        center = Offset(size.width / 2, size.height / 2)
                    )
                }
                CameraIcons(
                    drawableId = R.drawable.photo_camera,
                    description = "Take Photo"
                ) {

                }

            }


        }
    } else {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "The camera is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Camera permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            Text(textToShow)
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}


// Function to save the image to the gallery
private fun saveImageToGallery(context: Context, bitmap: Bitmap?) {
    bitmap?.let {
        // Use MediaStore to insert the image into the gallery
        val imageUri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        )

        imageUri?.let { uri ->
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }

            // Notify the gallery app that a new image has been added
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        }
    }
}




@Composable
private fun CameraIcons(
    drawableId : Int,
    description : String,
    onclick : () -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val radius = size.minDimension / 2 + 10.0f
            val strokeWidth = 10.dp.toPx()

            // Draw the circle border
            drawCircle(
                color = Color.LightGray,
                style = Stroke(width = strokeWidth),
                radius = radius,
                center = Offset(size.width / 2, size.height / 2)
            )
        }

        IconButton(onClick = onclick, modifier = Modifier.matchParentSize()) {
            Icon(
                imageVector = ImageVector.vectorResource(drawableId),
                contentDescription = description,
                modifier = Modifier.size(100.dp)
            )
        }
    }

}

