package com.formaloo.home.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.formaloo.home.R
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun FormImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_logo),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.FillWidth,
    )
}
