//import com.google.accompanist.permissions.rememberPermissionState
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.provider.MediaStore
import android.util.Log
import android.view.Surface
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.neeto.banknotedetector.R
import com.neeto.banknotedetector.components.CameraPreview
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neeto.banknotedetector.app.MainViewModel
import com.neeto.banknotedetector.data.Classification
import com.neeto.banknotedetector.data.ImageAnalyser
import com.neeto.banknotedetector.data.TFLiteClassifier
import com.neeto.banknotedetector.router.AppRouter
import com.neeto.banknotedetector.router.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable

fun CameraUsage(
    paddingValues: PaddingValues,
    scope : CoroutineScope,
    scaffoldState : BottomSheetScaffoldState,
    rotation : Int
) {
    val viewModel = viewModel<MainViewModel>()
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    if (cameraPermissionState.status.isGranted) {

        val contextCamera = LocalContext.current

        Box(modifier = Modifier
            .padding(paddingValues)
        ) {

            val cameraController = remember{ LifecycleCameraController(contextCamera)
                .apply {
                    setEnabledUseCases(
                        CameraController.IMAGE_CAPTURE
                    )

                }
            }
            CameraPreview(
                controller = cameraController,
                modifier = Modifier.fillMaxSize()
            )
            // if its Portrait orientation
            if(rotation == Surface.ROTATION_0){
                Row (
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    CameraIcons(
                        cameraController = cameraController,
                        scope = scope,
                        scaffoldState = scaffoldState,
                        sizeSideButtons = 50.dp,
                        viewModel = viewModel
                    )

                }
            }else{
                val alignment =  if (rotation == Surface.ROTATION_90) Alignment.CenterEnd else Alignment.CenterStart

                Column (
                    modifier = Modifier
                        .padding(20.dp)
                        .align(alignment)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally


                ){
                    CameraIcons(
                        cameraController = cameraController,
                        scope = scope,
                        scaffoldState = scaffoldState,
                        sizeSideButtons = 35.dp,
                        viewModel = viewModel

                    )

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





@Composable
private fun CameraIcon(
    drawableId : Int,
    description : String,
    sizeButton : Dp,
    onclick : () -> Unit
) {
    Box(
        modifier = Modifier
            .size(sizeButton)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CameraIcons(
    cameraController : LifecycleCameraController,
    scope: CoroutineScope,
    scaffoldState : BottomSheetScaffoldState,
    sizeSideButtons: Dp,
    viewModel: MainViewModel
){
    CameraIcon(
        drawableId = R.drawable.photo_library,
        description = "Open Gallery",
        sizeButton = sizeSideButtons,
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


//                        val matrix = Matrix().apply {
//                            postRotate(image.imageInfo.rotationDegrees.toFloat())
//                        }
//                        val rotatedBitmap = Bitmap.createBitmap(
//                            image.toBitmap(),
//                            0,
//                            0,
//                            image.width,
//                            image.height,
//                            matrix,
//                            true
//                        )

                        (viewModel::onTakePhoto)(image.toBitmap())
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
    CameraIcon(
        drawableId = R.drawable.ic_check,
        description = "Goto Results",
        sizeButton = sizeSideButtons
    ) {
        AppRouter.currentScreen.value = Screen.ResultsScreen
    }

}
