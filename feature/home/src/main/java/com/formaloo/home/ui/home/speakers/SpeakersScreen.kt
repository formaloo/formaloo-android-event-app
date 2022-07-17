package com.formaloo.home.ui.home.speakers

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.formaloo.home.FullScreenLoading
import com.formaloo.home.LoadingContent
import com.formaloo.home.ui.home.FormImage
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.home.vm.SpeakersViewModel
import com.formaloo.model.Converter
import com.formaloo.model.boards.BlockType
import com.formaloo.model.boards.block.Block
import com.formaloo.model.form.Fields
import com.formaloo.model.local.Speaker
import com.formaloo.model.RenderedData
import com.formaloo.model.Row
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.viewModel

@Composable
fun SpeakersScreen(
    block: Block,
    onSpeakerClick: (String, String, String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val vm by viewModel<SpeakersViewModel>()
    if (block.type == BlockType.GROUP.slug) {
        block?.sub_items?.let {
            if (it.isNotEmpty()) {
                vm.fetchBlock(it[0].block?.slug ?: "")

            }
        }

    }

    val viewState by vm.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it, modifier = Modifier.systemBarsPadding()) },
        backgroundColor = EventTheme.colors.uiBackground,

        ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        LoadingContent(
            empty = viewState.initialLoad,
            emptyContent = { FullScreenLoading() },
            loading = viewState.loading,
            onRefresh = { vm.fetchBlock(block.slug) },
            content = {
                SpeakersContent(
                    isRefreshing = viewState.loading,
                    imageField = viewState.imageField,
                    titleField = viewState.titleField,
                    block = viewState.speakersBlock,
                    scrollState = scrollState,
                    viewModel = vm,
                    onSpeakerClick
                )
            }
        )
    }

}

@Composable
fun SpeakersContent(
    isRefreshing: Boolean,
    imageField: Fields?,
    titleField: Fields?,
    block: Block?,
    scrollState: LazyListState,
    viewModel: SpeakersViewModel,
    onSpeakerClick: (String, String, String) -> Unit,

    ) {
    Column(
        modifier = Modifier.background(EventTheme.colors.uiBorder)
    ) {
        RowsList(viewModel, block, imageField, titleField, scrollState, onSpeakerClick)

    }

}

@Composable
fun RowsList(
    viewModel: SpeakersViewModel,
    block: Block?,
    imageField: Fields?,
    titleField: Fields?,
    scrollState: LazyListState,
    onSpeakerClick: (String, String, String) -> Unit,

    ) {
    val flow = viewModel.fetchSpeakerList(block?.slug ?: "", true)

    val lazyPagingItems: LazyPagingItems<Speaker> = flow.collectAsLazyPagingItems()

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
                SpeakerRow(Converter().to<Row>(it.row), imageField, titleField, onSpeakerClick)

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
    onSpeakerClick: (String, String, String) -> Unit
) {

    Column(
        modifier = Modifier
            .height(112.dp)
            .padding(bottom = 16.dp)
            .background(EventTheme.colors.uiBackground)

    ) {
        Row(modifier = Modifier.clickable(onClick = {
            it?.let {
                onSpeakerClick(it.slug, titleField?.slug ?: "", imageField?.slug ?: "")

            }
        })) {
            SpeakerImage(it?.rendered_data, imageField)


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
    var keyList = keys?.dropWhile {
        it == titleField?.slug || it == imageField?.slug
    }
    keyList = keyList?.subList(0, 2)
    Column() {
        keyList?.forEach {
            Text(
                text = renderedData?.get(it)?.raw_value?.toString() ?: "",
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,


                )
        }
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
fun SpeakerImage(renderedData: Map<String, RenderedData>?, imageField: Fields?) {
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

