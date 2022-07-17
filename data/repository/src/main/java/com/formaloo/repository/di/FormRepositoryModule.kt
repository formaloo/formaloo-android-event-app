package com.formaloo.repository.di

import com.formaloo.remote.di.remoteAllFormModulConstant
import com.formaloo.remote.di.remoteBoardsModulConstant
import com.formaloo.repository.FormzRepo
import com.formaloo.repository.FormzRepoImpl
import com.formaloo.repository.board.BoardRepo
import com.formaloo.repository.board.BoardRepoImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val formRepositoryModule = module(override = true) {

    single<BoardRepo>(named(boardsRepoConstants.RepoName)) {
        BoardRepoImpl(
            get(named(remoteBoardsModulConstant.DataSourceName)),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single<FormzRepo>(named(formAllRepoConstants.RepoName)) {
        FormzRepoImpl(
            get(named(remoteAllFormModulConstant.DataSourceName))
        )
    }

}


object boardsRepoConstants {
    val RepoName = "BoardsRepo"

}

object formAllRepoConstants {
    val RepoName = "FormzRepo"

}


