package com.formaloo.home.ui.gallery

import androidx.collection.ArrayMap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.formaloo.home.FullScreenLoading
import com.formaloo.home.LoadingContent
import com.formaloo.home.R
import com.formaloo.home.SimpleTopAppbarWithBack
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.home.vm.GalleryViewModel
import com.formaloo.model.Row
import com.formaloo.model.boards.block.Block
import com.formaloo.model.form.Fields
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.viewModel
import kotlin.math.ceil

@Composable
fun AlbumScreen(
    blockSlug: String,
    albumTitle: String,
    onPicClick: (String) -> Unit,
    upPress: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),

    ) {

    val vm by viewModel<GalleryViewModel>()
    vm.fetchAlbumBlock(blockSlug)

    val viewState by vm.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it, modifier = Modifier.systemBarsPadding()) },
        backgroundColor = EventTheme.colors.uiBackground,
        topBar = {
            SimpleTopAppbarWithBack(albumTitle, upPress, false)

        },

        ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        LoadingContent(
            empty = viewState.initialLoad,
            emptyContent = { FullScreenLoading() },
            loading = viewState.loading,
            onRefresh = { vm.fetchBlock(blockSlug) },
            content = {
                AlbumContent(
                    imageField = viewState.imageField,
                    block = viewState.albumBlock,
                    scrollState = scrollState,
                    viewModel = vm,
                    onPicClick = onPicClick

                )
            }
        )
    }

}

@Composable
fun AlbumContent(
    imageField: Fields?,
    block: Block?,
    scrollState: LazyListState,
    viewModel: GalleryViewModel,
    onPicClick: (String) -> Unit,

    ) {
    Column(
        modifier = Modifier.background(EventTheme.colors.uiBackground)
    ) {
        AlbumRowsList(
            viewModel,
            block,
            imageField,
            scrollState,
            onPicClick
        )

    }


}

@Composable
fun AlbumRowsList(
    viewModel: GalleryViewModel,
    block: Block?,
    imageField: Fields?,
    scrollState: LazyListState,
    onPicClick: (String) -> Unit,
) {
    val params = ArrayMap<String, Any>()
    val flow = viewModel.fetchGalleryList(block?.slug ?: "", true)

    val lazyPagingItems: LazyPagingItems<Row> =
        flow.collectAsLazyPagingItems()

    LazyColumn(
        state = scrollState,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = false
        )
    ) {
        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
//                FullScreenLoading()

            }
        }

        item {
            Column(
                modifier = Modifier
//                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
            ) {
                AlbumStaggeredVerticalGrid(
                    maxColumnWidth = 220.dp,
                    modifier = Modifier.padding(4.dp)
                ) {
                    lazyPagingItems.itemSnapshotList.forEach { item ->
                        item?.let {
                            AlbumRow(it, imageField, onPicClick)

                        }
                    }

                }
            }

        }

        lazyPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    //You can add modifier to manage load state when first time response page is loading
                }
                loadState.append is LoadState.Loading -> {
                    //You can add modifier to manage load state when next response page is loading
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    //You can use modifier to show error message
                }
            }
        }
    }
}

@Composable
fun AlbumRow(
    row: Row,
    imageField: Fields?,
    onPicClick: (String) -> Unit,
) {
    var imageURL = ""
    var photoFieldSlug = ""
    val renderedData = row?.rendered_data
    renderedData?.keys?.find {
        it == imageField?.slug
    }?.let {
        photoFieldSlug = it
        imageURL = renderedData[it]?.raw_value.toString()
    }

    GalleryItemsSource().addPhotoDetail(
        row.slug,
        row.rendered_data?.get(photoFieldSlug)?.raw_value?.toString() ?: ""
    )

    Surface(
        modifier = Modifier.padding(4.dp),
        color = EventTheme.colors.uiBackground,
        shape = MaterialTheme.shapes.medium
    ) {
        val featuredString = stringResource(id = R.string.app_name)
        ConstraintLayout(
            modifier = Modifier
                .clickable(
                    onClick = { onPicClick(row.slug ?: "") }
                )
                .semantics {
                    contentDescription = featuredString
                }
        ) {
            val (image) = createRefs()

            NetworkImage(
                url = imageURL,
                contentDescription = null,
                modifier = Modifier
//                    .aspectRatio(4f / 3f)
                    .constrainAs(image) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                    }
            )


        }
    }
}

@Composable
fun AlbumStaggeredVerticalGrid(
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




