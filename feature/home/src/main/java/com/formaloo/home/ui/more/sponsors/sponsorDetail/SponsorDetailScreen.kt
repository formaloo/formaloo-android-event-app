package com.formaloo.home.ui.more.sponsors.sponsorDetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import coil.compose.rememberImagePainter
import com.formaloo.home.R
import com.formaloo.home.ui.components.EventDivider
import com.formaloo.home.ui.components.EventSurface
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.home.ui.theme.Neutral8
import com.formaloo.home.vm.SponsorDetailViewModel
import com.formaloo.model.Converter
import com.formaloo.model.RenderedData
import com.formaloo.model.Row
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import org.koin.androidx.compose.viewModel
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min

@Composable
fun SponsorDetailScreen(
    rowSlug: String, titleFieldSlug: String, imageFieldSlug: String, upPress: () -> Unit
) {

    val vm by viewModel<SponsorDetailViewModel>()
    vm.getSponsorDetail(rowSlug)
    val viewState by vm.uiDetailState.collectAsState()
    Timber.e("imageFieldSlug ${imageFieldSlug}")
    Timber.e("titleFieldSlug ${titleFieldSlug}")
    Timber.e("rowSlug ${rowSlug}")
    val row = Converter().to<Row>(viewState.sponsorsDetail?.row)

    SponsorDetail(row, upPress, titleFieldSlug, imageFieldSlug)

}

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
private fun SponsorDetail(
    row: Row?,
    upPress: () -> Unit,
    titleFieldSlug: String,
    imageFieldSlug: String
) {
    val renderedData = row?.rendered_data
    Box(Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        Header()
        Body(renderedData, scroll, titleFieldSlug, imageFieldSlug)
        Title(renderedData, scroll.value, titleFieldSlug)
        Image(renderedData, imageFieldSlug, scroll.value)
        Up(upPress)
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(EventTheme.colors.tornado1))
    )
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .size(36.dp)
            .padding(8.dp)
            .background(
                color = Neutral8.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            tint = EventTheme.colors.iconInteractive,
            contentDescription = stringResource(R.string.app_name)
        )
    }
}

@Composable
private fun Body(
    renderedData: Map<String, RenderedData>?,
    scroll: ScrollState,
    titleFieldSlug: String,
    imageFieldSlug: String
) {
    val fieldsSlug = renderedData?.keys?.dropWhile {
        it == titleFieldSlug || it == imageFieldSlug
    }
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            EventSurface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))
                    fieldsSlug?.forEach { slug ->
                        renderedData[slug]?.let {
                            convertRawValueToString(it)

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
fun ValueText(value: String) {
    Text(
        text = value,
        style = MaterialTheme.typography.body1,
        color = EventTheme.colors.textHelp,
//                            maxLines = if (seeMore) 5 else Int.MAX_VALUE,
//                            overflow = TextOverflow.Ellipsis,
        modifier = HzPadding
    )

}

@Composable
private fun Title(renderedData: Map<String, RenderedData>?, scroll: Int, titleFieldSlug: String) {
    var titleTxt = "Sponsor"

    renderedData?.let {
        val titleRenderDataSlug = renderedData.keys.find {
            it == titleFieldSlug
        }
        renderedData[titleRenderDataSlug ?: ""]?.raw_value?.toString()?.let {
            titleTxt = it
        }
    }

    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }
    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .graphicsLayer { translationY = offset }
            .background(color = EventTheme.colors.uiBackground)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = titleTxt,
            style = MaterialTheme.typography.h4,
            color = EventTheme.colors.textSecondary,
            modifier = HzPadding
        )
//        Text(
//            text = sponsor.tagline,
//            style = MaterialTheme.typography.subtitle2,
//            fontSize = 20.sp,
//            color = EventTheme.colors.textHelp,
//            modifier = HzPadding
//        )
        Spacer(Modifier.height(4.dp))
//        Text(
//            text = formatPrice(sponsor.price),
//            style = MaterialTheme.typography.h6,
//            color = EventTheme.colors.textPrimary,
//            modifier = HzPadding
//        )

        Spacer(Modifier.height(8.dp))
        EventDivider()
    }
}

@Composable
private fun Image(
    renderedData: Map<String, RenderedData>?,
    imageFieldSlug: String,
    scroll: Int
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

    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFraction = (scroll / collapseRange).coerceIn(0f, 1f)

    CollapsingImageLayout(
        collapseFraction = collapseFraction,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {

        SponsorImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier.aspectRatio(1f)
        )
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFraction: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
//        check(measurables.size == 1)

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

@Composable
fun SponsorImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    EventSurface(
        color = Color.LightGray,
        elevation = elevation,
        shape = CircleShape,
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
            contentScale = ContentScale.Inside,
        )
    }
}


@Composable
fun convertRawValueToString(renderedData: RenderedData) {
    val value = renderedData.value
    val renderDataTitle = renderedData.title

    if (value != null)
        when (value) {
            is String -> {
                CreateKetValueUi(renderDataTitle, value)

            }
            is Map<*, *> -> {
                val keyList = value.keys as List<String>
                keyList.forEach { key ->
                    val mapItemTitle = key
                    val mapItemValue = value[key]
                    when (mapItemValue) {
                        is String -> {
                            CreateKetValueUi(mapItemTitle, mapItemValue)

                        }
                        is Map<*, *> -> {
                            val innerMapItemTitle = key
                            val innerMapItemValue = mapItemValue[key]?.toString() ?: "---"

                            CreateKetValueUi(innerMapItemTitle, innerMapItemValue)


                        }
                    }


                }
            }
            is Number -> {
                CreateKetValueUi(renderDataTitle, "$value")

            }
            else -> {
                CreateKetValueUi(renderDataTitle, "$value")
            }
        }

}

@Composable
fun CreateKetValueUi(title: String?, value: String) {
    TitleText(title)
    Spacer(Modifier.height(8.dp))
//                        var seeMore by remember { mutableStateOf(true) }
    ValueText(value)

    Spacer(Modifier.height(16.dp))

}

