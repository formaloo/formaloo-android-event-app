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
import com.formaloo.model.Row
import com.formaloo.repository.board.BoardRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * UI state for the Home screen
 */
data class MoreUiState(
    val moreBlock: Block? = null,
    val albumBlock: Block? = null,
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


class MoreViewModel(private val repository: BoardRepo) : BaseViewModel() {


    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(MoreUiState(loading = true))
    val uiState: StateFlow<MoreUiState> = _uiState.asStateFlow()

    fun fetchAlbumBlock(blockSlug: String) {
        // Ui state is refreshing
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.getBlock(SHAREDBOARDADDRESS, blockSlug)
            _uiState.update {
                when (result) {
                    is Result.Success -> {
                        Timber.e("refreshMoreBlock ${result.data.data}")
                        val block = result.data.data?.block
                        it.copy(
                            albumBlock = block,
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

    fun fetchMoreMenu(blockSlug: String) {
        // Ui state is refreshing
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.getBlock(SHAREDBOARDADDRESS, blockSlug)
            _uiState.update {
                when (result) {
                    is Result.Success -> {
                        val moreMenu = result.data.data?.block
                        it.copy(
                            moreBlock = moreMenu,
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

    fun fetchBlock(blockSlug: String) {
        fetchMoreMenu(blockSlug)
    }

    fun fetchMoreList(blockSlug: String, force: Boolean): Flow<PagingData<Row>> {
        Timber.e("RowsList ${blockSlug}")

        val params = ArrayMap<String, Any>()
        return repository.fetchGalley(SHAREDBOARDADDRESS, blockSlug, force, params)
            .cachedIn(viewModelScope)
    }
}
