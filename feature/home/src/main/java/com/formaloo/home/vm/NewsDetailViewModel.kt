package com.formaloo.home.vm


import androidx.lifecycle.viewModelScope
import com.formaloo.common.base.BaseViewModel
import com.formaloo.model.local.News
import com.formaloo.repository.board.BoardRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * UI state for the NewsDetail screen
 */
data class NewsDetailUiState(
    val newsDetail: News? = null,
    val loading: Boolean = false,
) {
    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = newsDetail == null && loading
}


class NewsDetailViewModel(private val repository: BoardRepo) : BaseViewModel() {

    // UI state exposed to the UI
    private val _uiDetailState = MutableStateFlow(NewsDetailUiState(loading = true))
    val uiDetailState: StateFlow<NewsDetailUiState> = _uiDetailState.asStateFlow()

    fun getNewsDetail(newsSlug: String) = viewModelScope.launch {
//        _uiDetailState.update {
//            it.copy(
//                loading = true
//            )
//        }

        val news = withContext(Dispatchers.IO) { repository.getNews(newsSlug) }
        _uiDetailState.update {
            it.copy(
                newsDetail = news,
                loading = false
            )
        }
    }
}
