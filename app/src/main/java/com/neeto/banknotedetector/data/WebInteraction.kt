package com.neeto.banknotedetector.data

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OpenLinkInBrowser() {
    val context = LocalContext.current as ComponentActivity

    Button(
        onClick = {
            openLinkInBrowser(
                context,
                "https://www.scitepress.org/PublicationsDetail.aspx?ID=VMbUbjCQUMo%3D&t=1"
            )
        },
        modifier = Modifier.padding(5.dp)
    ) {
        Text(text = "Clique aqui para acessar o artigo", fontSize = 12.sp)
    }
}

private fun openLinkInBrowser(context: ComponentActivity, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

