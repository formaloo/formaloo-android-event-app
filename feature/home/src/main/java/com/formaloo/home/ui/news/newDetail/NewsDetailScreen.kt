package com.formaloo.home.ui.news.newDetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.formaloo.common.Constants
import com.formaloo.home.R
import com.formaloo.home.SimpleTopAppbarWithBack
import com.formaloo.home.ui.components.EventDivider
import com.formaloo.home.ui.components.EventSurface
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.model.Converter
import com.formaloo.model.RenderedData
import com.formaloo.model.Row
import coil.compose.rememberImagePainter
import com.formaloo.home.vm.NewsDetailViewModel
import com.google.accompanist.insets.navigationBarsPadding
import org.koin.androidx.compose.viewModel
import timber.log.Timber

@Composable
fun NewsDetailScreen(
    rowSlug: String, titleFieldSlug: String, imageFieldSlug: String, upPress: () -> Unit
) {

    val vm by viewModel<NewsDetailViewModel>()
    vm.getNewsDetail(rowSlug)
    val viewState by vm.uiDetailState.collectAsState()

    val row = Converter().to<Row>(viewState.newsDetail?.row)

//    NewsDetail(row, upPress, titleFieldSlug, imageFieldSlug)
    Detail(row, upPress, titleFieldSlug, imageFieldSlug, true, {})
}

private val BottomBarHeight = 56.dp
private val HzPadding = Modifier.padding(bottom = 16.dp)

@Composable
private fun NewsDetail(
    row: Row?,
    upPress: () -> Unit,
    titleFieldSlug: String,
    imageFieldSlug: String,
    state: LazyListState = rememberLazyListState(),

    ) {
    val renderedData = row?.rendered_data
    LazyColumn(
        state = state,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        item {
            Image(renderedData, imageFieldSlug)
        }
        item {
            Title(renderedData, titleFieldSlug)
        }
        item {
            Body(renderedData, titleFieldSlug, imageFieldSlug)
        }


    }
}

@Composable
private fun Detail(
    row: Row?,
    upPress: () -> Unit,
    titleFieldSlug: String,
    imageFieldSlug: String,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        backgroundColor = EventTheme.colors.uiBackground,
        topBar = {
            SimpleTopAppbarWithBack(stringResource(id = R.string.news), upPress, false)
        },
        /*   bottomBar = {
               BottomBar(
                   row = row,
                   onUnimplementedAction = { },
                   isFavorite = isFavorite,
                   onToggleFavorite = onToggleFavorite
               )
           }*/
    ) { innerPadding ->
        NewsDetail(
            row = row,
            upPress = upPress,
            titleFieldSlug = titleFieldSlug,
            imageFieldSlug = imageFieldSlug
        )
    }
}

/**
 * Bottom bar for Article screen
 *
 * @param post (state) used in share sheet to share the post
 * @param onUnimplementedAction (event) called when the user performs an unimplemented action
 * @param isFavorite (state) if this post is currently a favorite
 * @param onToggleFavorite (event) request this post toggle it's favorite status
 */
@Composable
private fun BottomBar(
    row: Row?,
    onUnimplementedAction: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(EventTheme.colors.brand)
            .navigationBarsPadding()
            .height(56.dp)
            .fillMaxWidth()
    ) {
        IconButton(onClick = onUnimplementedAction) {
            Icon(
                imageVector = Icons.Filled.ThumbUpOffAlt,
                contentDescription = stringResource(R.string.save)
            )
        }
        BookmarkButton(
            isBookmarked = isFavorite,
            onClick = onToggleFavorite
        )
        val context = LocalContext.current
        IconButton(onClick = {
//                sharePost(post, context)
        }) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.share)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onUnimplementedAction) {
            Icon(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = stringResource(R.string.app_name)
            )
        }
    }
}

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clickLabel = stringResource(
        if (isBookmarked) R.string.ok else R.string.cancel
    )
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        IconToggleButton(
            checked = isBookmarked,
            onCheckedChange = { onClick() },
            modifier = modifier.semantics {
                // Use a custom click label that accessibility services can communicate to the user.
                // We only want to override the label, not the actual action, so for the action we pass null.
                this.onClick(label = clickLabel, action = null)
            }
        ) {
            Icon(
                imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                contentDescription = null // handled by click label of parent
            )
        }
    }
}


@Composable
private fun Body(
    renderedData: Map<String, RenderedData>?,
    titleFieldSlug: String,
    imageFieldSlug: String
) {
    val fieldsSlug = renderedData?.keys

    Column() {
        EventSurface(Modifier.fillMaxWidth()) {
            Column {
                Spacer(Modifier.height(4.dp))
                fieldsSlug?.forEach { slug ->
                    if (slug != titleFieldSlug && slug != imageFieldSlug) {
                        renderedData[slug]?.let {
                            convertRawValueToString(it)

                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                EventDivider()
                Spacer(
                    modifier = Modifier
                        .padding(bottom = BottomBarHeight)
                        .navigationBarsPadding(start = false, end = false)
                        .height(8.dp)
                )
            }
        }
    }

}

@Composable
fun TitleText(title: String?) {
    Text(
        text = title ?: "---",
        style = MaterialTheme.typography.overline,
        color = EventTheme.colors.textHelp,
        modifier = HzPadding
    )

}

@Composable
fun ValueText(value: String, txtColor: Color, txtStyle: TextStyle) {
    Text(
        text = value,
        style = txtStyle,
        color = txtColor,
//                            maxLines = if (seeMore) 5 else Int.MAX_VALUE,
//                            overflow = TextOverflow.Ellipsis,
        modifier = HzPadding
    )

}

@Composable
fun ValueWEBText(value: String, txtColor: Color, txtStyle: TextStyle) {
    // Creating an annonated string
    val annotatedText = buildAnnotatedString {
        // We attach this *URL* annotation to the following content
        // until `pop()` is called
        pushStringAnnotation(
            tag = "URL",
            annotation = value
        )
        withStyle(
            style = SpanStyle(
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("Read More")
        }

        pop()
    }
    val mUriHandler = LocalUriHandler.current

    Timber.e("WebSite $value")
    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.h4,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "URL", start = offset,
                end = offset
            )
                .firstOrNull()?.let { stringAnnotation ->
                    Timber.e("stringAnnotation ${stringAnnotation.item}")

                    mUriHandler.openUri(stringAnnotation.item)
                }
        },
        modifier = HzPadding,

        )

}

@Composable
private fun Title(renderedData: Map<String, RenderedData>?, titleFieldSlug: String) {
    var titleTxt = "News"

    renderedData?.let {
        val titleRenderDataSlug = renderedData.keys.find {
            it == titleFieldSlug
        }
        renderedData[titleRenderDataSlug ?: ""]?.raw_value?.toString()?.let {
            titleTxt = it
        }
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .background(color = EventTheme.colors.uiBackground)
    ) {
        Text(
            text = titleTxt,
            style = MaterialTheme.typography.h4,
            color = EventTheme.colors.textSecondary,
            modifier = HzPadding,

            )
    }
}

@Composable
private fun Image(
    renderedData: Map<String, RenderedData>?,
    imageFieldSlug: String,
) {
    var imageUrl: String? = null
    renderedData?.let {
        val titleRenderDataSlug = renderedData.keys.find {
            it == imageFieldSlug
        }
        renderedData[titleRenderDataSlug ?: ""]?.raw_value?.toString()?.let {
            imageUrl = it
        }
    }
    val imageModifier = Modifier
        .heightIn(min = 180.dp)
        .fillMaxWidth()
        .padding(bottom = 16.dp)
        .clip(shape = MaterialTheme.shapes.medium)
    NewsImage(
        imageUrl = imageUrl,
        contentDescription = null,
        modifier = imageModifier
    )
}


@Composable
fun NewsImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    EventSurface(
        color = Color.LightGray,
        elevation = elevation,
        shape = RectangleShape,
        modifier = modifier
    ) {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    crossfade(true)
                    placeholder(drawableResId = R.drawable.ic_logo)
                }
            ),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}


@Composable
fun convertRawValueToString(renderedData: RenderedData) {
    val value = renderedData.value
    val renderDataTitle = renderedData.title

    if (value != null) {
        when (value) {
            is String -> {

                when (renderedData.type) {
                    Constants.SHORT_TEXT -> {
                        ValueText(
                            value,
                            EventTheme.colors.textSecondary,
                            MaterialTheme.typography.subtitle1
                        )

                    }
                    Constants.WEBSITE -> {
                        ValueWEBText(
                            value,
                            EventTheme.colors.textSecondary,
                            MaterialTheme.typography.subtitle1
                        )

                    }
                    else -> {
                        ValueText(value, EventTheme.colors.textHelp, MaterialTheme.typography.body1)

                    }
                }


            }
            is Map<*, *> -> {

                val keyList = value.keys as List<String>
                keyList.forEach { key ->
                    val mapItemTitle = key
                    val mapItemValue = value[key]
                    when (mapItemValue) {
                        is String -> {
                            ValueText(
                                mapItemValue,
                                EventTheme.colors.textLink,
                                MaterialTheme.typography.body1
                            )

                        }
                        is Map<*, *> -> {
                            val innerMapItemTitle = key
                            val innerMapItemValue = mapItemValue[key]?.toString() ?: "---"

                            ValueText(
                                innerMapItemValue,
                                EventTheme.colors.textLink,
                                MaterialTheme.typography.body1
                            )


                        }
                    }


                }
            }
            is Number -> {
                ValueText("$value", EventTheme.colors.textSecondary, MaterialTheme.typography.body1)

            }
            else -> {
                ValueText("$value", EventTheme.colors.textSecondary, MaterialTheme.typography.body1)
            }
        }

    }

    Spacer(Modifier.height(8.dp))

}

@Composable
fun CreateKetValueUi(title: String?, value: String) {
    ValueText(value, EventTheme.colors.textHelp, MaterialTheme.typography.body1)
    Spacer(Modifier.height(8.dp))

}

@Composable
fun CreateTagTxt(title: String?, value: String) {
    ValueText(value, EventTheme.colors.textHelp, MaterialTheme.typography.body1)
    Spacer(Modifier.height(8.dp))

}

