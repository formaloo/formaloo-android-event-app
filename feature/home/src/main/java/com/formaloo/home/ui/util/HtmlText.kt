package com.formaloo.home


import android.os.Build
import android.text.Html
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.formaloo.common.MyTagHandler
import com.formaloo.home.ui.util.CoilImageGetter

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
//        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
        update = {
            val imageGetter = CoilImageGetter(it)

            it.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(
                    html, Html.FROM_HTML_MODE_LEGACY, imageGetter,
                    MyTagHandler()
                )
            } else {
                Html.fromHtml(html, imageGetter, MyTagHandler())
            }
        }
    )
}
