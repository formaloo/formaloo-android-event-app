package com.formaloo.eventApp.di

import com.formaloo.common.BuildConfig.BASE_URL
import com.formaloo.common.BuildConfig.X_API_KEY
import com.formaloo.home.di.boardModule
import com.formaloo.local.di.formBuilderLocalModule
import com.formaloo.remote.di.createRemoteAllFormzModule
import com.formaloo.remote.di.createRemoteBoardModule
import com.formaloo.repository.di.formRepositoryModule

val appComponent = listOf(
    createRemoteAllFormzModule(BASE_URL, X_API_KEY),
    createRemoteBoardModule(BASE_URL, X_API_KEY),
    formBuilderLocalModule,
    formRepositoryModule,
    boardModule
)
