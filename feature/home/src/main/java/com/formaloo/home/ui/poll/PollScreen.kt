package com.formaloo.home.ui.poll

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.formaloo.home.FullScreenLoading
import com.formaloo.home.LoadingContent
import com.formaloo.home.SimpleTopAppbar
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.model.boards.block.Block
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.charts.Pie
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.anychart.palettes.RangeColors
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.viewModel
import timber.log.Timber
import com.formaloo.home.R
import com.formaloo.home.vm.PollViewModel

@Composable
fun PollScreen(
    block: Block,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    openWebView: (String) -> Unit,

) {

    val vm by viewModel<PollViewModel>()
    vm.fetchBlock(block.slug)

    val viewState by vm.uiState.collectAsState()
    val chartState by vm.chartUiState.collectAsState()
    val scrollState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it, modifier = Modifier.systemBarsPadding()) },
        backgroundColor = EventTheme.colors.uiBackground,
        topBar = {
            SimpleTopAppbar(stringResource(id = R.string.feedback), false)

        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = viewState.pollBlock?.title ?: "",
                        color = EventTheme.colors.uiBackground
                    )
                },
                backgroundColor = EventTheme.colors.brand,
                contentColor = EventTheme.colors.uiBackground,
                onClick = {
                    openWebView(viewState.pollBlock?.slug?:"")
                },
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
            onRefresh = { vm.fetchBlock(block.slug) },
            content = {
                PollContent(
                    chartBlock = chartState.chartBlock,
                    pollBlock = viewState.pollBlock,
                    scrollState = scrollState,
                    viewModel = vm,

                )
            }
        )
    }

}

@Composable
fun PollContent(
    chartBlock: Block?,
    pollBlock: Block?,
    scrollState: LazyListState,
    viewModel: PollViewModel,

    ) {

    LazyColumn(
        state = scrollState,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = false
        )
    ) {
        item {
            val context = LocalContext.current

            chartBlock?.form?.stats?.fields?.forEach { field ->
                AndroidView(factory = {
                    AnyChartView(context).apply {
                        Timber.e("AnyChartView ${field.title}")

                        val ll = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        ll.height = 800
                        layoutParams = ll

                        val pie = AnyChart.pie()
                        pie.setOnClickListener(object :
                            ListenersInterface.OnClickListener(arrayOf("x", "value")) {
                            override fun onClick(event: Event) {
                                Toast.makeText(
                                    context,
                                    event.data["x"].toString() + ":" + event.data["value"],
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                        val data: MutableList<DataEntry> = ArrayList()
                        when (val readableStats = field.readable_stats) {
                            is Map<*, *> -> {
                                val keys = ArrayList(readableStats.keys)
                                keys.forEach { key ->
                                    try {
                                        val label = key as String
                                        when (val value = readableStats[label]) {
                                            is Double, is Float -> {
                                                val totalCount = value.toString().toFloat()
                                                data.add(ValueDataEntry(label, totalCount))
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Timber.e("Key is not String?? $e")
                                    }
                                }

                            }
                        }



                        pie.data(data)
                        pie.title(field.title)
                        pieSetting(pie)
                        setChart(pie)
                    }
                })

            }
        }


    }


}

private fun pieSetting(pie: Pie) {
    val colors = arrayOf(
        "#F82B60",
        "#CFDFFF",
        "#EDE3FE",
        "#72DDC3",
        "#FF9EB7",
        "#D1F7C4",
        "#FF6F2C",
        "#E08D00",
        "#FF08C2",
        "#7C39ED",
        "#2750AE",
        "#11AF22",
        "#06A09B",
        "#CDB0FF",
        "#9CC7FF",
        "#FFDAF6",
        "#20C933",
        "#FFEAB6",
        "#FFA981",
        "#D74D26"
    )
    colors.shuffle()
    val rangeColors = RangeColors.instantiate()
    rangeColors.items(colors, "")

    pie.palette(rangeColors)

    pie.labels().position("inside")
    pie.legend().title().enabled(true)
    pie.legend().title()
        .text(" ")
        .padding(0.0, 0.0, 10.0, 0.0)

    pie.legend()
        .position("bottom")
        .itemsLayout(LegendLayout.HORIZONTAL)
        .align(Align.LEFT)

}







