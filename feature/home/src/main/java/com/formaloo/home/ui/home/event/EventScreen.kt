package com.formaloo.home.ui.home.event

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.formaloo.home.FullScreenLoading
import com.formaloo.home.HtmlText
import com.formaloo.home.LoadingContent
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.model.boards.block.Block
import com.formaloo.model.form.Form
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.viewModel
import timber.log.Timber
import com.formaloo.home.R
import com.formaloo.home.vm.EventViewModel


@Composable
fun EventScreen(
    block: Block,
    registerBlock: Block,
    openWebView: (String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val vm by viewModel<EventViewModel>()
    vm.fetchBlock(block?.block?.slug ?: "")

    Timber.e("EventScreen registerBlock $registerBlock")
    val viewState by vm.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it, modifier = Modifier.systemBarsPadding()) },
        backgroundColor = EventTheme.colors.uiBackground,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = stringResource(id = R.string.register),color= EventTheme.colors.uiBackground)
                },
                backgroundColor =  EventTheme.colors.brand,
                contentColor = EventTheme.colors.uiBackground,
                onClick = { openWebView(registerBlock.block?.slug?:"")},
                modifier = Modifier
                    .navigationBarsPadding()
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        LoadingContent(
            empty = viewState.initialLoad,
            emptyContent = { FullScreenLoading() },
            loading = viewState.loading,
            onRefresh = {  vm.fetchBlock(block?.block?.slug ?: "") },
            content = {
                EventContent(
                    isRefreshing = viewState.loading,
                    eventForm = viewState.eventForm,
                    scrollState = scrollState

                )
            }
        )
    }


}

@Composable
fun EventContent(
    isRefreshing: Boolean, eventForm: Form?, scrollState: LazyListState
) {
    val modifier = Modifier.padding(bottom = 16.dp)

    eventForm?.let { form ->
        LazyColumn(
            modifier = modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            state = scrollState,
            contentPadding = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.systemBars,
                applyTop = false
            )
        ) {
            item {

                EventTitle(form.title ?: "", modifier)
                EventLogo(form.logo, form.title ?: "")
                EventDescription(form.description ?: "", modifier)
                EventExtraData(form, modifier)

            }

        }
    }


}

@Composable
fun EventExtraData(form: Form, modifier: Modifier) {

    form.fields_list?.forEach { field ->
//        Text(
//            modifier = Modifier.padding(bottom = 16.dp),
//            text = field.title ?: "",
//            style = MaterialTheme.typography.subtitle1
//        )
        HtmlText(html = field.description ?: "", modifier)

    }

}

@Composable
fun EventDescription(description: String, modifier: Modifier) {
    HtmlText(html = description, modifier)

}

@Composable
fun EventLogo(logo: String?, title: String) {
    Timber.e("EventLogo $logo")
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(logo)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_logo),
        contentDescription = title,
        modifier = Modifier.padding(bottom = 0.dp).fillMaxSize().height(150.dp).padding(bottom = 16.dp),
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
fun EventTitle(title: String, modifier: Modifier) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.subtitle1
    )

}





