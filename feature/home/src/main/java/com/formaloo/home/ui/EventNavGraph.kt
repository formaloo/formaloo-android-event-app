package com.formaloo.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.formaloo.home.ui.MainDestinations.ALBUM_BLOCK_SLUG
import com.formaloo.home.ui.MainDestinations.ALBUM_BLOCK_TITLE
import com.formaloo.home.ui.MainDestinations.BLOCK_SLUG
import com.formaloo.home.ui.MainDestinations.IMAGE_FIELD_SLUG
import com.formaloo.home.ui.MainDestinations.INDEX
import com.formaloo.home.ui.MainDestinations.NEWS_KEY
import com.formaloo.home.ui.MainDestinations.PHOTO_FIELD_SLUG
import com.formaloo.home.ui.MainDestinations.REGISTER_SLUG
import com.formaloo.home.ui.MainDestinations.SPEAKER_KEY
import com.formaloo.home.ui.MainDestinations.TITLE_FIELD_SLUG
import com.formaloo.home.ui.gallery.AlbumScreen
import com.formaloo.home.ui.gallery.GalleryDetailScreen
import com.formaloo.home.ui.gallery.GalleryScreen
import com.formaloo.home.ui.home.HomeScreen
import com.formaloo.home.ui.home.register.RegisterScreen
import com.formaloo.home.ui.home.speakers.speakerDetail.SpeakerDetailScreen
import com.formaloo.home.ui.more.MoreScreen
import com.formaloo.home.ui.more.about.AboutScreen
import com.formaloo.home.ui.more.faq.FAQScreen
import com.formaloo.home.ui.more.sponsors.sponsorDetail.SponsorDetailScreen
import com.formaloo.home.ui.more.sponsors.SponsorsScreen
import com.formaloo.home.ui.news.NewsScreen
import com.formaloo.home.ui.news.newDetail.NewsDetailScreen
import com.formaloo.home.ui.poll.PollScreen
import com.formaloo.model.boards.block.Block
import timber.log.Timber

/**
 * Destinations used in the ([EventApp]).
 */
object MainDestinations {
    const val HOME_ROUTE = "home"
    const val SPEAKER_DETAIL_ROUTE = "speaker"
    const val REGISTER_EVENT = "register"
    const val SPEAKER_KEY = "speakerSlug"
    const val TITLE_FIELD_SLUG = "titleFieldSlug"
    const val IMAGE_FIELD_SLUG = "imageFieldSlug"
    const val SPEAKER_DETAIL = "speakerDetail"

    const val NEWS_DETAIL_ROUTE = "new"
    const val NEWS_KEY = "newsSlug"

    const val GALLERY_DETAIL_ROUTE = "gallery"
    const val PHOTO_FIELD_SLUG = "photoFieldSlug"

    const val REGISTER_SLUG = "registerSlug"

    const val Album_ROUTE = "album"

    const val More_MENU_ROUTE = "more"

    const val BLOCK_SLUG = "block_slug"

    const val INDEX = "index"

    const val SPONSOR_DETAIL_ROUTE = "sponsor"

    const val ALBUM_BLOCK_SLUG = "album_block_slug"
    const val ALBUM_BLOCK_TITLE = "album_block_title"
}

@Composable
fun EventNavGraph(

    modifier: Modifier = Modifier,
    tabs: ArrayList<Block>?,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.HOME_ROUTE,
) {
    if (tabs?.isNotEmpty() == true) {

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            navigation(
                route = MainDestinations.HOME_ROUTE,
                startDestination = tabs[0].slug
            ) {

                tabs.forEach {
                    when (tabs.indexOf(it)) {
                        0 -> {

                            composable(it.slug) { from ->
                                HomeScreen(
                                    it.slug,
                                    onSpeakerClick = { rowSlug, titleFieldSlug, imageFieldSlug ->
                                        if (from.lifecycleIsResumed()) {
                                            navController.navigate("${MainDestinations.SPEAKER_DETAIL_ROUTE}/${rowSlug}/${titleFieldSlug}/${imageFieldSlug}")
                                        }
                                    },
                                    openWebView = { registerBlockSlug ->
                                        if (from.lifecycleIsResumed()) {
                                            navController.navigate("${MainDestinations.REGISTER_EVENT}/${registerBlockSlug}")
                                        }
                                    },
                                )
                            }
                        }
                        1 -> {
                            composable(it.slug) { from ->
                                Timber.e("composable NewsScreen ")
                                NewsScreen(
                                    it,
                                    onNewsClick = { rowSlug, titleFieldSlug, imageFieldSlug ->
                                        if (from.lifecycleIsResumed()) {
                                            navController.navigate("${MainDestinations.NEWS_DETAIL_ROUTE}/${rowSlug}/${titleFieldSlug}/${imageFieldSlug}")
                                        }
                                    },
                                )
                            }
                        }
                        2 -> {

                            composable(it.slug) { from ->
                                GalleryScreen(
                                    it,
                                    onAlbumClick = { blockSlug, blockTitle ->
                                        if (from.lifecycleIsResumed()) {
                                            navController.navigate("${MainDestinations.Album_ROUTE}/$blockSlug/$blockTitle")
                                        }
                                    },
                                )
                            }
                        }
                        3 -> {

                            composable(it.slug) { from ->
                                PollScreen(
                                    it,
                                    openWebView = { registerBlockSlug ->
                                        if (from.lifecycleIsResumed()) {
                                            navController.navigate("${MainDestinations.REGISTER_EVENT}/${registerBlockSlug}")
                                        }
                                    },
                                )
                            }
                        }
                        4 -> {

                            composable(it.slug) { from ->
                                MoreScreen(
                                    it,
                                    onItemClick = { blockSlug, index ->
                                        if (from.lifecycleIsResumed()) {
                                            Timber.e("onItemClick $index")

                                            navController.navigate("${MainDestinations.More_MENU_ROUTE}/$blockSlug/$index")
                                        }
                                    },
                                )
                            }
                        }
                        else -> {
                            composable(it.slug) { from ->
                                MoreScreen(
                                    it,
                                    onItemClick = { blockSlug, index ->
                                        if (from.lifecycleIsResumed()) {
                                            Timber.e("onItemClick $index")

                                            navController.navigate("${MainDestinations.More_MENU_ROUTE}/$blockSlug/$index")
                                        }
                                    },
                                )
                            }
                        }
                    }

                }

                composable(
                    "${MainDestinations.SPEAKER_DETAIL_ROUTE}/{$SPEAKER_KEY}/{$TITLE_FIELD_SLUG}/{$IMAGE_FIELD_SLUG}",
                    arguments = listOf(navArgument(SPEAKER_KEY) { type = NavType.StringType })
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val speakerSlug = arguments.getString(SPEAKER_KEY) ?: ""
                    val titleFieldSlug = arguments.getString(TITLE_FIELD_SLUG) ?: ""
                    val imageFieldSlug = arguments.getString(IMAGE_FIELD_SLUG) ?: ""
                    SpeakerDetailScreen(
                        rowSlug = speakerSlug,
                        titleFieldSlug = titleFieldSlug,
                        imageFieldSlug = imageFieldSlug,
                        upPress = {
                            navController.navigateUp()
                        }
                    )
                }

                composable(
                    "${MainDestinations.NEWS_DETAIL_ROUTE}/{$NEWS_KEY}/{$TITLE_FIELD_SLUG}/{$IMAGE_FIELD_SLUG}",
                    arguments = listOf(navArgument(NEWS_KEY) { type = NavType.StringType })
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val newSlug = arguments.getString(NEWS_KEY) ?: ""
                    val titleFieldSlug = arguments.getString(TITLE_FIELD_SLUG) ?: ""
                    val imageFieldSlug = arguments.getString(IMAGE_FIELD_SLUG) ?: ""
                    NewsDetailScreen(
                        rowSlug = newSlug,
                        titleFieldSlug = titleFieldSlug,
                        imageFieldSlug = imageFieldSlug,
                        upPress = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    "${MainDestinations.Album_ROUTE}/{$ALBUM_BLOCK_SLUG}/{$ALBUM_BLOCK_TITLE}",
                    arguments = listOf(navArgument(ALBUM_BLOCK_SLUG) { type = NavType.StringType })
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val blockSlug = arguments.getString(ALBUM_BLOCK_SLUG) ?: ""
                    val albumTitle = arguments.getString(ALBUM_BLOCK_TITLE) ?: ""
                    AlbumScreen(
                        blockSlug = blockSlug,
                        albumTitle = albumTitle,
                        onPicClick = { rowSlug ->
                            if (backStackEntry.lifecycleIsResumed()) {
                                navController.navigate("${MainDestinations.GALLERY_DETAIL_ROUTE}/$rowSlug")
                            }
                        },
                        upPress = {
                            navController.navigateUp()
                        }
                    )
                }
                composable(
                    "${MainDestinations.GALLERY_DETAIL_ROUTE}/{$PHOTO_FIELD_SLUG}",
                    arguments = listOf(navArgument(PHOTO_FIELD_SLUG) { type = NavType.StringType })
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val rowSlug = arguments.getString(PHOTO_FIELD_SLUG) ?: ""
                    GalleryDetailScreen(
                        rowSlug = rowSlug,
                        upPress = {
                            navController.navigateUp()
                        }
                    )
                }

                composable(
                    "${MainDestinations.REGISTER_EVENT}/{$REGISTER_SLUG}",
                    arguments = listOf(navArgument(REGISTER_SLUG) { type = NavType.StringType })
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val registerSlug = arguments.getString(REGISTER_SLUG) ?: ""
                    RegisterScreen(
                        registerSlug = registerSlug,
                        upPress = {
                            navController.navigateUp()
                        }
                    )
                }
                composable(
                    "${MainDestinations.More_MENU_ROUTE}/{${BLOCK_SLUG}}/{${INDEX}}",
                    arguments = listOf(navArgument(BLOCK_SLUG) { type = NavType.StringType })
                ) { backStackEntry ->
                    Timber.e("composable ${backStackEntry.destination}")
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val blockSlug = arguments.getString(BLOCK_SLUG) ?: ""
                    val index = arguments.getString(INDEX)
                    Timber.e("INDEX $index")
                    when (index) {
                        "0" -> {
                            SponsorsScreen(
                                blockSlug = blockSlug,
                                onSponsorClick = { rowSlug, titleFieldSlug, imageFieldSlug ->
                                    if (backStackEntry.lifecycleIsResumed()) {
                                        navController.navigate("${MainDestinations.SPONSOR_DETAIL_ROUTE}/$rowSlug/$titleFieldSlug/$imageFieldSlug")
                                    }
                                },
                                upPress = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        "1" -> {
                            AboutScreen(
                                blockSlug = blockSlug,
                                upPress = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        "2" -> {

                            FAQScreen(
                                blockSlug = blockSlug,
                                onFAQClick = { rowSlug, titleFieldSlug, imageFieldSlug ->

                                },
                                upPress = {
                                    navController.navigateUp()
                                }
                            )
                        }
                    }

                }

                composable(
                    "${MainDestinations.SPONSOR_DETAIL_ROUTE}/{$SPEAKER_KEY}/{$TITLE_FIELD_SLUG}/{$IMAGE_FIELD_SLUG}",
                    arguments = listOf(navArgument(SPEAKER_KEY) { type = NavType.StringType })
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val sponsorSlug = arguments.getString(SPEAKER_KEY) ?: ""
                    val titleFieldSlug = arguments.getString(TITLE_FIELD_SLUG) ?: ""
                    val imageFieldSlug = arguments.getString(IMAGE_FIELD_SLUG) ?: ""
                    SponsorDetailScreen(
                        rowSlug = sponsorSlug,
                        titleFieldSlug = titleFieldSlug,
                        imageFieldSlug = imageFieldSlug,
                        upPress = {
                            navController.navigateUp()
                        }
                    )
                }

            }
        }

    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

