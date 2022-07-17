package com.formaloo.home.ui.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.formaloo.home.R
import com.formaloo.home.ui.theme.EventTheme
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

/**
 * A wrapper around [Image] and [rememberImagePainter], setting a
 * default [contentScale] and showing content while loading.
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    placeholderColor: Color? = EventTheme.colors.brand
) {
    Box(modifier) {
        val painter = rememberImagePainter(
            data = url,
            builder = {
                placeholder(drawableResId = R.drawable.ic_logo)
            }
        )

        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
        )

//        if (painter.state is ImagePainter.State.Loading && placeholderColor != null) {
//            Spacer(
//                modifier = Modifier
//                    .matchParentSize()
//                    .background(placeholderColor)
//            )
//        }
    }
}
