package com.formaloo.home.ui.home.timeline

import android.util.ArrayMap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.formaloo.common.Constants
import com.formaloo.common.converDateStrToStr
import com.formaloo.common.convertTimeStrToDate
import com.formaloo.common.convertTimeToStr
import com.formaloo.home.FullScreenLoading
import com.formaloo.home.LoadingContent
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.home.vm.TimelineViewModel
import com.formaloo.model.Converter
import com.formaloo.model.RenderedData
import com.formaloo.model.Row
import com.formaloo.model.boards.BlockType
import com.formaloo.model.boards.block.Block
import com.formaloo.model.local.TimeLine
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.viewModel
import timber.log.Timber
import java.util.*

//2022-06-24
//03:00:00
@Composable
fun TimeLineScreen(
    block: Block,
    onTimelineClick: (String, String, String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val vm by viewModel<TimelineViewModel>()
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
                TimeLineContent(
                    isRefreshing = viewState.loading,
                    block = viewState.timelineBlock,
                    scrollState = scrollState,
                    viewModel = vm,
                    onTimelineClick
                )
            }
        )
    }

}


@Composable
fun TimeLineContent(
    isRefreshing: Boolean,
    block: Block?,
    scrollState: LazyListState,
    viewModel: TimelineViewModel,
    onTimeLineClick: (String, String, String) -> Unit,

    ) {
    Column(
        modifier = Modifier.background(EventTheme.colors.uiBorder)
    ) {
        RowsList(viewModel, block, scrollState, onTimeLineClick)

    }

}

@Composable
fun RowsList(
    viewModel: TimelineViewModel,
    block: Block?,
    scrollState: LazyListState,
    onTimeLineClick: (String, String, String) -> Unit,

    ) {
    val flow = viewModel.fetchTimeList(block?.slug ?: "", true)

    val lazyPagingItems: LazyPagingItems<TimeLine> = flow.collectAsLazyPagingItems()

    LazyColumn(
        state = scrollState,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = false
        )
    ) {
        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
//              FullScreenLoading()
            }
        }

        item {
            val sortTimeLine = sortTimeLine(lazyPagingItems.itemSnapshotList.items)

            sortTimeLine?.let { dateMap ->
                val datesStrList = dateMap.keys
                val newList = datesStrList.sorted()

                Column(
                    modifier = Modifier
                        .background(EventTheme.colors.uiBackground)

                ) {
                    newList.forEach { dateStr ->

                        CreateDateHeader(dateStr)

                        val timeMap = dateMap[dateStr]

                        val timeKeys = timeMap?.keys?.sorted()

                        timeKeys?.forEach { timeStr ->
                            val row = timeMap[timeStr]

                            Row(modifier = Modifier.clickable(onClick = {})) {
                                CreateTimeHeader(timeStr)
                                CreateTimeItems(row)


                            }


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

val strColorMap = ArrayMap<String, Color>()

@Composable
fun CreateTagsItems(renderedData: RenderedData?) {
    val value = renderedData?.value?.toString() ?: ""
    val catsList = value.split(",")

    Row(modifier = Modifier.padding(0.dp)) {
        catsList.forEachIndexed { index, cat ->

            val color = if (strColorMap.contains(cat)) {
                strColorMap[cat] ?: Color.Blue
            } else {
                val rnd = Random()
                val color = Color(255, rnd.nextInt(256), rnd.nextInt(256), 255)
                strColorMap[cat] = color
                color
            }

            Row(modifier = Modifier.padding(0.dp)) {

                Card(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color)
                        .wrapContentHeight(CenterVertically)
                        .padding(8.dp),

                    content = {
                        Box(
                            modifier = Modifier

                                .clip(CircleShape)
                                .background(color)
                                .wrapContentHeight(CenterVertically)
                        ) {

                        }
                    }

                )

                Text(
                    text = cat ?: "",
                    modifier = Modifier
                        .padding(end = 4.dp, start = 4.dp),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Light

                )
            }
        }

    }


}

fun sortTimeLine(timeLineList: List<TimeLine>): ArrayMap<String, ArrayMap<String, Row>> {
    val rowList = arrayListOf<Row>()
    timeLineList.forEach { tl ->
        Converter().to<Row>(tl.row)?.let { r ->
            rowList.add(r)

        }
    }

    var dateStr = ""
    var timeStr = ""

    val rowMap =
        ArrayMap<String, ArrayMap<String, Row>>() // date: time : renderedDataList

    rowList.forEach { row ->
        val renderedData = row.rendered_data
        val dateRD = renderedData?.values?.findLast {
            it.type == "date"
        }
        val timeRD = renderedData?.values?.findLast {
            it.type == "time"
        }


        dateStr = dateRD?.value.toString()
        timeStr = timeRD?.value.toString()

        val lastTimeMap = rowMap[dateStr] ?: ArrayMap<String, Row>()
//        val list = lastTimeMap[timeStr] ?: arrayListOf()
//        val timeMapList = ArrayList<RenderedData>(list)
//        timeMapList.addAll(ArrayList(extraRDValues))
        lastTimeMap[timeStr] = row
        rowMap[dateStr] = lastTimeMap


    }

    return rowMap
}


@Composable
fun CreateTimeItems(row: Row?) {
    val rd = row?.rendered_data


    val extraRDValues = rd?.values?.dropWhile {
        it.type == "date" && it.type == "time"
    } ?: arrayListOf()//list RD



    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        var firstItemSlug = ""

        extraRDValues?.forEachIndexed { index, renderedData ->
            Timber.e("CreateTimeItems $renderedData")
            val fw = if (index == 0 || renderedData.slug.equals(firstItemSlug)) {
                firstItemSlug = renderedData.slug ?: ""
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }

            val type = renderedData.type
            val rdValue = renderedData.value?.toString() ?: ""



            if (!type.equals(Constants.DATE)) {
                if (!type.equals(Constants.TIME)) {
                    if (type == Constants.MULTI_SELECT) {
                        CreateTagsItems(renderedData)

                    } else {
                        Text(
                            text = rdValue,
                            fontWeight = fw,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(top = 4.dp)


                        )
                    }

                }
            } else {

            }

        }


    }
}

@Composable
fun CreateTimeHeader(timeStr: String) {
    val convertTimeStrToDate = convertTimeStrToDate(timeStr)
    val value = convertTimeToStr(convertTimeStrToDate ?: Date()) ?: timeStr

    var timeAA = ""
    val timeTxt = if (value.contains("pm") || value.contains("PM")) {
        timeAA = "PM"
        value.replace("PM", "")
    } else if (value.contains("am") || value.contains("AM")) {
        timeAA = "AM"
        value.replace("AM", "")

    } else {
        value
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentWidth(CenterHorizontally),
    ) {
        Text(
            text = timeTxt,
            modifier = Modifier.padding(bottom = 4.dp),
            color = EventTheme.colors.brand,
            style = MaterialTheme.typography.body1,

            )
        Text(
            text = timeAA,
            color = EventTheme.colors.brand,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.wrapContentWidth(CenterHorizontally)
        )
    }

}

@Composable
fun CreateDateHeader(dateStr: String) {
    var date = converDateStrToStr(dateStr)

    if (date.contains("-")) {
        date = date.replace("-", " ")
    }

    Timber.e("SimpleDateFormat$date")

    Text(
        text = date ?: dateStr,
        modifier = Modifier
            .padding(bottom = 24.dp, top = 18.dp)
            .fillMaxWidth()
            .wrapContentWidth(align = Alignment.CenterHorizontally),
        fontWeight = FontWeight.Normal,
        style = MaterialTheme.typography.h5

    )

}



