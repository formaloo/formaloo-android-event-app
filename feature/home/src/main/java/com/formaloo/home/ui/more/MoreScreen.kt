package com.formaloo.home.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.formaloo.home.FullScreenLoading
import com.formaloo.home.LoadingContent
import com.formaloo.home.R
import com.formaloo.home.SimpleTopAppbar
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.home.vm.MoreViewModel
import com.formaloo.model.boards.block.Block
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.viewModel
import kotlin.math.ceil

@Composable
fun MoreScreen(
    block: Block,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onItemClick: (String, String) -> Unit

) {

    val vm by viewModel<MoreViewModel>()
    vm.fetchBlock(block.slug)

    val viewState by vm.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it, modifier = Modifier.systemBarsPadding()) },
        backgroundColor = EventTheme.colors.uiBackground,
        topBar = {
            SimpleTopAppbar(stringResource(id = R.string.more), false)

        },

        ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        LoadingContent(
            empty = viewState.initialLoad,
            emptyContent = { FullScreenLoading() },
            loading = viewState.loading,
            onRefresh = { vm.fetchBlock(block.slug) },
            content = {
                MoreContent(
                    block = viewState.moreBlock,
                    scrollState = scrollState,
                    viewModel = vm,
                    onItemClick = onItemClick

                )
            }
        )
    }

}

@Composable
fun MoreContent(
    block: Block?,
    scrollState: LazyListState,
    viewModel: MoreViewModel,
    onItemClick: (String, String) -> Unit,

    ) {
    Column(
//        modifier = Modifier.background(EventTheme.colors.uiBorder)
    ) {
        RowsList(
            viewModel,
            block,
            scrollState,
            onItemClick
        )

    }


}

@Composable
fun RowsList(
    viewModel: MoreViewModel,
    block: Block?,
    scrollState: LazyListState,
    onItemClick: (String, String) -> Unit,
) {
    var albumsList = block?.items ?: arrayListOf<Block>()

    LazyColumn(
        state = scrollState,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = false
        ), modifier = Modifier
            .background(EventTheme.colors.uiBorder)


    ) {

        item {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
            ) {
                albumsList.forEachIndexed { index, item ->
                    MoreRow(item, index, onItemClick)

                }
            }

        }


    }
}

@Composable
fun MoreRow(
    block: Block,
    index: Int,
    onItemClick: (String, String) -> Unit,
) {

    Row(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, bottom = 16.dp)
            .background(EventTheme.colors.uiBackground)
            .height(60.dp)
            .clickable { onItemClick(block.block?.slug ?: "", index.toString()) }
            .fillMaxWidth()

    ) {
        val icon = when (index) {
            0 -> {
                Icons.Filled.Info
            }
            1 -> {
                Icons.Filled.Info
            }
            2 -> {
                Icons.Filled.Info
            }
            else -> {
                Icons.Filled.Info
            }
        }
        Icon(
            imageVector =icon,
            contentDescription = stringResource(R.string.more),
            tint = EventTheme.colors.uiBorder,
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 4.dp)
                .wrapContentHeight(CenterVertically)
        )

        Text(
            text = block.title ?: "",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentHeight(CenterVertically), textAlign = TextAlign.Center
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




