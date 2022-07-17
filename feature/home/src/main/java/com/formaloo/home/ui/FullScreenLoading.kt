package com.formaloo.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Full screen circular progress indicator
 */
@Composable
 fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(EventTheme.colors.uiFloated)
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}
