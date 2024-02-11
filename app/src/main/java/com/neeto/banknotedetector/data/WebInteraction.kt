package com.neeto.banknotedetector.data

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun OpenLinkInBrowser() {
    val context = LocalContext.current as ComponentActivity

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(color = Color.Blue)
        ) {
            append("Clique aqui para abrir o link")
            addStringAnnotation(
                tag = "LINK",
                annotation = "https://www.scitepress.org/PublicationsDetail.aspx?ID=VMbUbjCQUMo%3D&t=1",
                start = 0,
                end = 25
            )
        }
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "LINK", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    openLinkInBrowser(context, annotation.item)
                }
        },
        modifier = Modifier
            .padding(16.dp)
            .clickable { /* Handle click on non-link text if needed */ }
    )
}

private fun openLinkInBrowser(context: ComponentActivity, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

