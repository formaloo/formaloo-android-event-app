package com.formaloo.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.formaloo.home.ui.components.InsetAwareTopAppBar
import com.formaloo.home.ui.theme.EventTheme

@Composable
fun SimpleTopAppbarWithBack(title: String, upPress: () -> Unit, scrolled: Boolean) {


    InsetAwareTopAppBar(
        backgroundColor = EventTheme.colors.uiBackground,
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)

            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    color = LocalContentColor.current,
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.CenterHorizontally)

                )
            }
        },
        navigationIcon = {
            IconButton(onClick = upPress) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.app_name),
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        elevation = if (!scrolled) 1.dp else 4.dp,
    )
}

@Composable
fun SimpleTopAppbar(title: String, scrolled: Boolean) {

    InsetAwareTopAppBar(
        backgroundColor = EventTheme.colors.uiBackground,
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    color = LocalContentColor.current,
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                )
            }
        },
        elevation = if (!scrolled) 1.dp else 4.dp,
    )
}
