package com.example.prison_app.ui.stuff

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.prison_app.R

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    drawableID : Int
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(ImageDecoderDecoder.Factory())
            }
            else
                add(GifDecoder.Factory())
        }
        .build()

    Image(modifier = modifier,
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = drawableID).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ), contentDescription = "Gif")
}

@Composable
fun ScreenWithGif(
    drawableID : Int
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        verticalArrangement = Arrangement.Center) {
        GifImage(modifier = Modifier
            .padding(12.dp)
            .align(Alignment.CenterHorizontally),
            drawableID = drawableID)
    }
}

@Composable
fun DialogWithGif(
    drawableID : Int,
    onDismissDialog : () -> Unit
) {
    Dialog(onDismissRequest = onDismissDialog) {
        ScreenWithGif(drawableID = drawableID)
    }
}

@Preview
@Composable
private fun GifPreview() {
    DialogWithGif(drawableID = R.drawable.eating){

    }
}