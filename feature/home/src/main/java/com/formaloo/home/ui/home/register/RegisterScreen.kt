package com.formaloo.home.ui.home.register

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.formaloo.home.FullScreenLoading
import com.formaloo.home.LoadingContent
import com.formaloo.home.SimpleTopAppbarWithBack
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.home.ui.util.isScrolled
import com.formaloo.model.form.Form
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.viewModel
import timber.log.Timber
import com.formaloo.home.R
import com.formaloo.home.vm.RegisterViewModel

@Composable
fun RegisterScreen(
    registerSlug: String, upPress: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val vm by viewModel<RegisterViewModel>()
    vm.fetchBlock(registerSlug)

    val viewState by vm.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it, modifier = Modifier.systemBarsPadding()) },
        backgroundColor = EventTheme.colors.uiBackground,
        topBar = {
            SimpleTopAppbarWithBack(
                viewState.registerForm?.title ?: "",
                upPress,
                scrollState.isScrolled
            )
        },
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        // Declare a string that contains a url


        LoadingContent(
            empty = viewState.initialLoad,
            emptyContent = { FullScreenLoading() },
            loading = viewState.loading,
            onRefresh = { vm.fetchBlock(registerSlug) },
            content = {
                RegisterContent(
                    isRefreshing = viewState.loading,
                    registerForm = viewState.registerForm,
                    scrollState = scrollState

                )
            }
        )
    }


}


@Composable
fun RegisterContent(
    isRefreshing: Boolean, registerForm: Form?, scrollState: LazyListState
) {
    val modifier = Modifier.padding(bottom = 16.dp)

    Timber.e("registerForm $registerForm")

    registerForm?.let { form ->
        val mUrl = stringResource(id = R.string.base_url) + form.address

        Column() {
            LazyColumn(
                modifier = modifier,
                state = scrollState,
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.systemBars,
                    applyTop = false
                )
            ) {

                item {


                    var backEnabled by remember { mutableStateOf(false) }
                    var webView: WebView? = null
                    AndroidView(
                        modifier = modifier,
                        factory = { context ->
                            WebView(context).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                webViewClient = object : WebViewClient() {
                                    override fun onPageStarted(
                                        view: WebView,
                                        url: String?,
                                        favicon: Bitmap?
                                    ) {
                                        backEnabled = view.canGoBack()
                                    }
                                }
                                initWebView(settings)
                                webView?.setInitialScale(1)

                                loadUrl(mUrl)
                                webView = this
                            }
                        },
                        update = {
                            webView = it
                        })

                    BackHandler(enabled = backEnabled) {
                        webView?.goBack()
                    }


                }
            }

        }

    }


}

private fun initWebView(settings: WebSettings) {

    settings.userAgentString = "Android"
    settings.loadWithOverviewMode = true
    settings.setJavaScriptEnabled(true)
    settings.useWideViewPort = true
    settings.databaseEnabled = true
    settings.allowContentAccess = true
    settings.allowFileAccessFromFileURLs = true
    settings.domStorageEnabled = true
    settings.allowFileAccess = true
    settings.setGeolocationEnabled(true)
    settings.setAppCacheEnabled(true)
    settings.setSupportMultipleWindows(true)
    settings.cacheMode = WebSettings.LOAD_DEFAULT
}
