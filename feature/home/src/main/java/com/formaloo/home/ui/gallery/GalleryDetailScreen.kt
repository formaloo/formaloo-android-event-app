package com.formaloo.home.ui.gallery

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.formaloo.home.R
import com.formaloo.home.SimpleTopAppbarWithBack
import com.formaloo.home.ui.theme.EventTheme
import timber.log.Timber

@Composable
fun GalleryDetailScreen(rowSlug: String, upPress: () -> Unit) {
    var imageUrl = ""

    val source = GalleryItemsSource()
    val photoSource = source.getPhotoSource()
    if (photoSource.keys.contains(rowSlug)) {
        imageUrl = photoSource[rowSlug] ?: ""

        Timber.e("photoFieldSlug $imageUrl")


    }

    Timber.e("")


    Scaffold(
        backgroundColor = EventTheme.colors.uiBackground,
        topBar = {
            SimpleTopAppbarWithBack(stringResource(id = R.string.gallery), upPress, false)

        },

        ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(4.dp),
            color = EventTheme.colors.uiBackground,
            shape = MaterialTheme.shapes.medium
        ) {
            val featuredString = stringResource(id = R.string.app_name)
            ConstraintLayout(
                modifier = Modifier
                    .semantics {
                        contentDescription = featuredString
                    }
            ) {
                val (image) = createRefs()

                NetworkImage(
                    url = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(image) {
                            centerHorizontallyTo(parent)
                            top.linkTo(parent.top)
                        }
                )


            }
        }
    }
}
