package com.formaloo.home.ui.news

import androidx.collection.ArrayMap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.formaloo.home.FullScreenLoading
import com.formaloo.home.LoadingContent
import com.formaloo.home.R
import com.formaloo.home.SimpleTopAppbar
import com.formaloo.home.ui.home.FormImage
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.home.vm.NewsViewModel
import com.formaloo.model.Converter
import com.formaloo.model.boards.block.Block
import com.formaloo.model.form.Fields
import com.formaloo.model.local.News
import com.formaloo.model.Row
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.viewModel

@Composable
fun NewsScreen(
    block: Block,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onNewsClick: (String, String, String) -> Unit,
) {
    val vm by viewModel<NewsViewModel>()

    vm.fetchBlock(block.slug)

    val viewState by vm.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it, modifier = Modifier.systemBarsPadding()) },
        backgroundColor = EventTheme.colors.uiBackground,
        topBar = {
            SimpleTopAppbar(stringResource(id = R.string.news), false)

        },
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        LoadingContent(
            empty = viewState.initialLoad,
            emptyContent = { FullScreenLoading() },
            loading = viewState.loading,
            onRefresh = { vm.fetchBlock(block.slug) },
            content = {
                NewsContent(
                    isRefreshing = viewState.loading,
                    imageField = viewState.imageField,
                    titleField = viewState.titleField,
                    block = viewState.newsBlock,
                    scrollState = scrollState,
                    viewModel = vm,
                    onNewsClick = onNewsClick
                )
            }
        )
    }

}

@Composable
fun NewsContent(
    isRefreshing: Boolean,
    imageField: Fields?,
    titleField: Fields?,
    block: Block?,
    scrollState: LazyListState,
    viewModel: NewsViewModel,
    onNewsClick: (String, String, String) -> Unit,
) {
    Column(
        modifier = Modifier.background(EventTheme.colors.uiBorder)
    ) {
        RowsList(viewModel, block, imageField, titleField, scrollState, onNewsClick)

    }

}

@Composable
fun RowsList(
    viewModel: NewsViewModel,
    block: Block?,
    imageField: Fields?,
    titleField: Fields?,
    scrollState: LazyListState,
    onNewsClick: (String, String, String) -> Unit,
) {
    val params = ArrayMap<String, Any>()
    val flow = viewModel.fetchNewsList(block?.slug ?: "", true)

    val lazyPagingItems: LazyPagingItems<News> = flow.collectAsLazyPagingItems()

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

        items(lazyPagingItems.itemSnapshotList) { item ->
            item?.let {
                SpeakerRow(Converter().to<Row>(it.row), imageField, titleField, onNewsClick)

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
fun SpeakerRow(
    it: Row?,
    imageField: Fields?,
    titleField: Fields?,
    onNewsClick: (String, String, String) -> Unit,
) {

    Column(
        modifier = Modifier
            .height(112.dp)
            .padding(bottom = 16.dp)
            .background(EventTheme.colors.uiBackground)
    ) {
        Row(modifier = Modifier.clickable(onClick = {
            onNewsClick(
                it?.slug ?: "",
                titleField?.slug ?: "",
                imageField?.slug ?: ""
            )
        })) {
            SpeakerImage(it, imageField)


            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                SpeakerName(it, titleField)
                SpeakerExtraData(it, titleField, imageField)

            }
        }
    }

}

@Composable
fun SpeakerExtraData(it: Row?, titleField: Fields?, imageField: Fields?) {
    val renderedData = it?.rendered_data
    val keys = renderedData?.keys
    val keyList = keys?.dropWhile {
        it == titleField?.slug || it == imageField?.slug
    }
    if (keyList?.isNotEmpty() == true) {
        Text(
            text = renderedData[keyList[0]]?.raw_value?.toString() ?: "",
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,

            )
    }


}

@Composable
fun SpeakerName(it: Row?, titleField: Fields?) {
    val renderedData = it?.rendered_data
    renderedData?.keys?.find {
        it == titleField?.slug
    }?.let {

        Text(
            text = renderedData[it]?.value?.toString() ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            style = MaterialTheme.typography.subtitle1

        )
    }


}

@Composable
fun SpeakerImage(it: Row?, imageField: Fields?) {
    val renderedData = it?.rendered_data
    renderedData?.keys?.find {
        it == imageField?.slug
    }?.let {
        FormImage(
            imageUrl = renderedData[it]?.raw_value.toString(),
            contentDescription = null,
            modifier = Modifier.aspectRatio(1f)
        )
    }

}
