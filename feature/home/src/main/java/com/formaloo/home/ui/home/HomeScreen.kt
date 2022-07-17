package com.formaloo.home.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.formaloo.home.FullScreenLoading
import com.formaloo.home.LoadingContent
import com.formaloo.home.ui.home.event.EventScreen
import com.formaloo.home.ui.home.speakers.SpeakersScreen
import com.formaloo.home.ui.home.timeline.TimeLineScreen
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.home.vm.HomeGroupViewModel
import com.formaloo.model.boards.block.Block
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import org.koin.androidx.compose.viewModel
import timber.log.Timber

@Composable
fun HomeScreen(
    menuBlockSlug: String,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onSpeakerClick: (String, String, String) -> Unit,
    openWebView: (String) -> Unit,
) {
    val homeVM by viewModel<HomeGroupViewModel>()

    homeVM.fetchBlock(menuBlockSlug)

    val viewState by homeVM.state.collectAsState()


    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it, modifier = Modifier.systemBarsPadding()) },
        backgroundColor = EventTheme.colors.uiBackground
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        LoadingContent(
            empty = viewState.initialLoad,
            emptyContent = { FullScreenLoading() },
            loading = viewState.refreshing,
            onRefresh = { homeVM.fetchBlock(menuBlockSlug) },
            content = {
                HomeContent(
                    isRefreshing = viewState.refreshing,
                    selectedHomeCategory = viewState.selectedHomeCategory,
                    homeCategories = viewState.homeCategories,
                    onCategorySelected = homeVM::onHomeCategorySelected,
                    modifier = modifier,
                    onSpeakerClick = onSpeakerClick,
                    openWebView = openWebView

                )
            }
        )
    }

}


@OptIn(ExperimentalPagerApi::class) // HorizontalPager is experimental
@Composable
fun HomeContent(
    isRefreshing: Boolean,
    selectedHomeCategory: Block?,
    homeCategories: List<Block>,
    onCategorySelected: (Block) -> Unit,
    modifier: Modifier,
    onSpeakerClick: (String, String, String) -> Unit,
    openWebView: (String) -> Unit
) {

    Timber.e("selectedHomeCategory $selectedHomeCategory")
    Column(modifier = modifier) {


//        if (isRefreshing) {
//            // TODO show a progress indicator or similar
//        }

        if (homeCategories.isNotEmpty()) {
            HomeCategoryTabs(
                categories = homeCategories,
                selectedCategory = selectedHomeCategory,
                onCategorySelected = onCategorySelected
            )
        }

        if (homeCategories.isNotEmpty())
            when (selectedHomeCategory) {
                homeCategories[0] -> {
                    EventScreen(selectedHomeCategory, homeCategories[3], openWebView)
                }
                homeCategories[1] -> {
                    SpeakersScreen(selectedHomeCategory, onSpeakerClick)

                }
                homeCategories[2] -> {
                    TimeLineScreen(selectedHomeCategory, onSpeakerClick)

                }


            }


    }

}


@Composable
private fun HomeCategoryTabs(
    categories: List<Block>,
    selectedCategory: Block?,
    onCategorySelected: (Block) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        HomeCategoryTabIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedIndex])
        )
    }

    TabRow(
        selectedTabIndex = selectedIndex,
        indicator = indicator,
        modifier = modifier,
        backgroundColor = EventTheme.colors.uiBackground,
    ) {
        categories.forEachIndexed { index, category ->
            Timber.e("TabRow ${category.title}")
            if (index < 3) {
                Tab(
                    selected = index == selectedIndex,
                    onClick = { onCategorySelected(category) },
                    text = {
                        Text(
                            text = category.title ?: "",
                            style = MaterialTheme.typography.body2
                        )
                    }
                )
            }

        }
    }

}


@Composable
fun HomeCategoryTabIndicator(
    modifier: Modifier = Modifier,
    color: Color = EventTheme.colors.textHelp
) {
    Spacer(
        modifier
            .padding(horizontal = 24.dp)
            .height(4.dp)
            .background(color, RoundedCornerShape(topStartPercent = 100, topEndPercent = 100))
    )
}
