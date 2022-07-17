package com.formaloo.home.vm


import android.util.ArrayMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.formaloo.common.Constants.SHAREDBOARDADDRESS
import com.formaloo.common.base.BaseViewModel
import com.formaloo.model.Result
import com.formaloo.model.boards.block.Block
import com.formaloo.model.form.Fields
import com.formaloo.model.local.News
import com.formaloo.repository.board.BoardRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * UI state for the Home screen
 */
data class NewsUiState(
    val newsBlock: Block? = null,
    val imageField: Fields? = null,
    val titleField: Fields? = null,
    val loading: Boolean = false,
    val errorMessages: List<String> = emptyList()
) {
    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = imageField == null && titleField == null && errorMessages.isEmpty() && loading
}


class NewsViewModel(private val repository: BoardRepo) : BaseViewModel() {


    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(NewsUiState(loading = true))
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    fun fetchNewsBlock(blockSlug: String) {
        // Ui state is refreshing
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.getBlock(SHAREDBOARDADDRESS, blockSlug)
            _uiState.update {
                when (result) {
                    is Result.Success -> {
                        Timber.e("refreshNewsBlock ${result.data.data}")
                        val block = result.data.data?.block
                        it.copy(
                            newsBlock = block,
                            imageField = block?.featured_image_field,
                            titleField = block?.items_field,
                            loading = false
                        )
                    }
                    is Result.Error -> {
                        val errorMessages = it.errorMessages
                        it.copy(errorMessages = errorMessages, loading = false)
                    }

                }
            }
        }
    }

    fun fetchNewsMenu(blockSlug: String) {
        // Ui state is refreshing
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.getBlock(SHAREDBOARDADDRESS, blockSlug)
            when (result) {
                is Result.Success -> {
                    val newsMenu = result.data.data?.block
                    newsMenu?.items?.let { blockList ->
                        if (blockList.isNotEmpty()) {
                            val newsBlock = blockList[0].block
                            fetchNewsBlock(newsBlock?.slug?:"")
                        }
                    }

                }
                is Result.Error -> {

                }

            }

        }
    }

    fun fetchBlock(blockSlug: String) {
        fetchNewsMenu(blockSlug)
    }

    fun fetchNewsList(blockSlug: String, force: Boolean): Flow<PagingData<News>> {
        val params = ArrayMap<String, Any>()
        return repository.fetchNews(SHAREDBOARDADDRESS, blockSlug, force, params)
            .cachedIn(viewModelScope)
    }
}
