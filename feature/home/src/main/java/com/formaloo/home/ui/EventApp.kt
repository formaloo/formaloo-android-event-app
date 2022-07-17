package com.formaloo.home.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.formaloo.home.ui.EventBottomBar
import com.formaloo.home.R
import com.formaloo.home.ui.components.EventScaffold
import com.formaloo.home.ui.theme.EventTheme
import com.formaloo.model.boards.board.Board
import com.google.accompanist.insets.ProvideWindowInsets
import timber.log.Timber

@Composable
fun EventApp(board: Board) {
    Timber.e("EventApp")
    Timber.e("$board")
    ProvideWindowInsets {
        board.blocks?.leftSideBar?.removeIf {
            it.type == "stats"
        }

        EventTheme {
            val navController = rememberNavController()
            EventScaffold(
                bottomBar = {
                    EventBottomBar(
                        navController = navController,
                        tabs = board.blocks?.leftSideBar ?: arrayListOf()
                    )
                }
            ) { innerPaddingModifier ->
                EventNavGraph(
                    modifier = Modifier.padding(innerPaddingModifier),
                    tabs = board.blocks?.leftSideBar ?: arrayListOf(),
                    navController = navController,
                )
            }
        }

    }
}

@Suppress("DEPRECATION")
private fun checkIfOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false

        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    } else {
        cm.activeNetworkInfo?.isConnectedOrConnecting == true
    }
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.no_internet)) },
        text = { Text(text = stringResource(R.string.no_internet)) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.try_again))
            }
        }
    )
}

