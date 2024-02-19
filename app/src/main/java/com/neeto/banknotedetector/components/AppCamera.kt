//import com.google.accompanist.permissions.rememberPermissionState
import android.Manifest
import android.util.Log
import android.view.Surface
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.neeto.banknotedetector.R
import com.neeto.banknotedetector.app.MainViewModel
import com.neeto.banknotedetector.components.CameraPreview
import com.neeto.banknotedetector.data.rotate
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
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )
    var flashState = remember{ mutableStateOf(false) }
    val flashIcon = if (flashState.value) R.drawable.ic_flash_on else R.drawable.ic_flash_off


    if (cameraPermissionState.status.isGranted) {

        val contextCamera = LocalContext.current

        Box(modifier = Modifier
            .padding(paddingValues)
        ) {

            val cameraController = remember {
                LifecycleCameraController(contextCamera).apply {
                    setEnabledUseCases(CameraController.IMAGE_CAPTURE)

                }
            }
            // Torch Control for each recomposition
            cameraController.enableTorch(flashState.value)


            val lifecycleOwner = LocalLifecycleOwner.current

            // show camera feed in the app
            CameraPreview(
                lifecycleOwner = lifecycleOwner,
                controller = cameraController,
                modifier = Modifier.fillMaxSize(),
            )



            // if its Portrait orientation
            if(rotation == Surface.ROTATION_0){
                // Icone de Flash
                Row(modifier = Modifier
                    .padding(20.dp),

                ){

                    CameraIcon(drawableId = flashIcon, description = "Flash", sizeButton = 50.dp) {
                        flashState.value = !flashState.value

                    }
                }
                
                // Demais icones de ação
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
                val alignmentIcons =  if (rotation == Surface.ROTATION_90) Alignment.CenterEnd else Alignment.CenterStart
                val alignmentFlash = if (rotation == Surface.ROTATION_90) Alignment.BottomStart else Alignment.TopEnd

                Column(modifier = Modifier
                    .padding(20.dp)
                    .align(alignmentFlash)
                    ){
                    CameraIcon(drawableId = flashIcon, description = "Flash", sizeButton = 50.dp) {
                        flashState.value = !flashState.value
                    }
                }


                Column (
                    modifier = Modifier
                        .padding(20.dp)
                        .align(alignmentIcons)
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
    var isPhotoTaken = false

    Canvas(modifier = Modifier
        .size(100.dp)
        .clickable(onClick = {
            cameraController.takePicture(
                cameraContext,
                object : OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)

                        isPhotoTaken = true
                        val rotationDegrees = image.imageInfo.rotationDegrees
                        val rotatedBitmap = image
                            .toBitmap()
                            .rotate(rotationDegrees.toFloat())

                        (viewModel::onTakePhoto)(rotatedBitmap)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        Log.e("Camera", "Couldn't take photo: ", exception)
                        isPhotoTaken = false
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


    if (isPhotoTaken){
        isPhotoTaken = false

        LaunchedEffect(scaffoldState.snackbarHostState) {
            /* TODO: snackbar com texto "foto tirada com sucesso" */
            scaffoldState.snackbarHostState.showSnackbar(
                "Imagem registrada com sucesso!",
                duration = SnackbarDuration.Long
            )
        }
    }


}


