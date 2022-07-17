package com.formaloo.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.formaloo.common.Constants
import com.formaloo.common.Constants.SHAREDBOARDADDRESS
import com.formaloo.home.vm.BoardViewModel
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import timber.log.Timber

class HomeActivity : AppCompatActivity(), KoinComponent {
    private val boardVM: BoardViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCenter.start(
            application, Constants.AppCenterKey,
            Analytics::class.java, Crashes::class.java
        )

        boardVM.retrieveSharedBoardDetail(SHAREDBOARDADDRESS)
        boardVM.board.observe(this) {
            it?.let {
                Timber.e("it")
                setContent {
                    EventApp(it)
                }
            }

        }


    }

}
