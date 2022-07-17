package com.formaloo.home.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.formaloo.home.FullScreenLoading
import com.formaloo.home.LoadingContent
import com.formaloo.home.R
import com.formaloo.home.SimpleTopAppbar
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.home.vm.GalleryViewModel
import com.formaloo.model.boards.block.Block
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.viewModel
import kotlin.math.ceil

@Composable
fun GalleryScreen(
    block: Block,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onAlbumClick: (String, String) -> Unit

) {

    val vm by viewModel<GalleryViewModel>()
    vm.fetchBlock(block.slug)

    val viewState by vm.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it, modifier = Modifier.systemBarsPadding()) },
        backgroundColor = EventTheme.colors.uiBackground,
        topBar = {
            SimpleTopAppbar(stringResource(id = R.string.gallery), false)

        },

        ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        LoadingContent(
            empty = viewState.initialLoad,
            emptyContent = { FullScreenLoading() },
            loading = viewState.loading,
            onRefresh = { vm.fetchBlock(block.slug) },
            content = {
                GalleryContent(
                    block = viewState.galleryBlock,
                    scrollState = scrollState,
                    viewModel = vm,
                    onAlbumClick = onAlbumClick

                )
            }
        )
    }

}

@Composable
fun GalleryContent(
    block: Block?,
    scrollState: LazyListState,
    viewModel: GalleryViewModel,
    onAlbumClick: (String, String) -> Unit,

    ) {
    Column(
        modifier = Modifier.background(EventTheme.colors.uiBackground)
    ) {
        RowsList(
            viewModel,
            block,
            scrollState,
            onAlbumClick
        )

    }


}

@Composable
fun RowsList(
    viewModel: GalleryViewModel,
    block: Block?,
    scrollState: LazyListState,
    onAlbumClick: (String, String) -> Unit,
) {
    var albumsList = arrayListOf<Block>()
    block?.items?.let {
        if (it.isNotEmpty()) {
            albumsList = it[0].sub_items ?: arrayListOf()

        }

    }
    LazyColumn(
        state = scrollState,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = false
        )
    ) {

        item {
            Column(
                modifier = Modifier
//                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
            ) {
                StaggeredVerticalGrid(
                    maxColumnWidth = 220.dp,
                    modifier = Modifier.padding(4.dp)
                ) {
                    albumsList.forEach { item ->
                        GalleryRow(item, onAlbumClick)

                    }

                }
            }

        }


    }
}

@Composable
fun GalleryRow(
    block: Block,
    onAlbumClick: (String, String) -> Unit,
) {
    Column(
        modifier = Modifier
            .wrapContentSize(Center)
            .fillMaxWidth()

    ) {
        Surface(
            modifier = Modifier
                .padding(4.dp)
                .border(
                    1.dp, EventTheme.colors.uiBorder,
                    RectangleShape
                )
                .padding(4.dp)
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .clickable(
                    onClick = { onAlbumClick(block.block?.slug ?: "", block.title ?: "") }
                )
                .background(EventTheme.colors.uiBorder),
            color = EventTheme.colors.uiBorder,
            shape = MaterialTheme.shapes.large,
        ) {

            Icon(
                imageVector = Icons.Filled.Photo,
                contentDescription = stringResource(R.string.gallery),
                tint = EventTheme.colors.uiBackground
            )


        }

        Text(
            text = block.title ?: "",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .wrapContentWidth(CenterHorizontally)
                .padding(bottom = 16.dp)

        )
    }
}

@Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    maxColumnWidth: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        check(constraints.hasBoundedWidth) {
            "Unbounded width not supported"
        }
        val columns = ceil(constraints.maxWidth / maxColumnWidth.toPx()).toInt()
        val columnWidth = constraints.maxWidth / columns
        val itemConstraints = constraints.copy(maxWidth = columnWidth)
        val colHeights = IntArray(columns) { 0 } // track each column's height
        val placeables = measurables.map { measurable ->
            val column = shortestColumn(colHeights)
            val placeable = measurable.measure(itemConstraints)
            colHeights[column] += placeable.height
            placeable
        }

        val height = colHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
            ?: constraints.minHeight
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            val colY = IntArray(columns) { 0 }
            placeables.forEach { placeable ->
                val column = shortestColumn(colY)
                placeable.place(
                    x = columnWidth * column,
                    y = colY[column]
                )
                colY[column] += placeable.height
            }
        }
    }
}

private fun shortestColumn(colHeights: IntArray): Int {
    var minHeight = Int.MAX_VALUE
    var column = 0
    colHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            column = index
        }
    }
    return column
}




