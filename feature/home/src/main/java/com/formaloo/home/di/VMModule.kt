package com.formaloo.home.di

import com.formaloo.home.vm.BoardViewModel
import com.formaloo.home.vm.GalleryViewModel
import com.formaloo.home.vm.HomeGroupViewModel
import com.formaloo.home.vm.EventViewModel
import com.formaloo.home.vm.RegisterViewModel
import com.formaloo.home.vm.SpeakersViewModel
import com.formaloo.home.vm.SpeakerDetailViewModel
import com.formaloo.home.vm.TimelineViewModel
import com.formaloo.home.vm.MoreViewModel
import com.formaloo.home.vm.AboutViewModel
import com.formaloo.home.vm.FAQViewModel
import com.formaloo.home.vm.SponsorsViewModel
import com.formaloo.home.vm.SponsorDetailViewModel
import com.formaloo.home.vm.NewsViewModel
import com.formaloo.home.vm.NewsDetailViewModel
import com.formaloo.home.vm.PollViewModel
import com.formaloo.repository.di.boardsRepoConstants
import com.formaloo.repository.di.formAllRepoConstants
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val boardModule = module {
    viewModel { BoardViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel {
        EventViewModel(
            get(named(boardsRepoConstants.RepoName)),
            get(named(formAllRepoConstants.RepoName))
        )
    }
    viewModel { HomeGroupViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { NewsViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { GalleryViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { MoreViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { SponsorsViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { SponsorDetailViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { FAQViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { PollViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { SpeakersViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { SpeakerDetailViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { NewsDetailViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel { TimelineViewModel(get(named(boardsRepoConstants.RepoName))) }
    viewModel {
        RegisterViewModel(
            get(named(boardsRepoConstants.RepoName)),
            get(named(formAllRepoConstants.RepoName))
        )
    }
    viewModel {
        AboutViewModel(
            get(named(boardsRepoConstants.RepoName)),
            get(named(formAllRepoConstants.RepoName))
        )
    }

}
