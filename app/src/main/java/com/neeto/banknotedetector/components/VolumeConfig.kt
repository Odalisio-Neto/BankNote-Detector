package com.neeto.banknotedetector.components

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun LockVolumeButtons() {
    val context = LocalContext.current
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        val volumeButtonLock = VolumeButtonLock(context)
        volumeButtonLock.lockVolumeButtons()

        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                volumeButtonLock.unlockVolumeButtons()
            }
        })
    }
}



class VolumeButtonLock(private val context: Context) {

    private var audioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun lockVolumeButtons() {
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_TOGGLE_MUTE,
            AudioManager.FLAG_VIBRATE
        )
        audioManager.allowedCapturePolicy = AudioAttributes.ALLOW_CAPTURE_BY_NONE


    }


    fun unlockVolumeButtons() {
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_UNMUTE,
            AudioManager.FLAG_SHOW_UI
        )
    }
}