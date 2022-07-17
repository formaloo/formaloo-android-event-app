package com.formaloo.home.vm


import android.util.ArrayMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.formaloo.common.Constants.SHAREDBOARDADDRESS
import com.formaloo.common.base.BaseViewModel
import com.formaloo.model.Result
import com.formaloo.model.boards.BlockType
import com.formaloo.model.boards.block.Block
import com.formaloo.model.Row
import com.formaloo.repository.board.BoardRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * UI state for the Home screen
 */
data class PollUiState(
    val pollBlock: Block? = null,
    val loading: Boolean = false,
    val errorMessages: List<String> = emptyList()
) {
    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = errorMessages.isEmpty() && loading
}

/**
 * UI state for the Home screen
 */
data class ChartUiState(
    val chartBlock: Block? = null,
    val loading: Boolean = false,
    val errorMessages: List<String> = emptyList()
) {
    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = errorMessages.isEmpty() && loading
}


class PollViewModel(private val repository: BoardRepo) : BaseViewModel() {


    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(PollUiState(loading = true))
    val uiState: StateFlow<PollUiState> = _uiState.asStateFlow()


    // UI state exposed to the UI
    private val _chartUiState = MutableStateFlow(ChartUiState(loading = true))
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()

    fun fetchPollMenu(blockSlug: String) {
        // Ui state is refreshing
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.getBlock(SHAREDBOARDADDRESS, blockSlug)
            _uiState.update {


                when (result) {
                    is Result.Success -> {
                        val pollMenu = result.data.data?.block

                        var chartBlock: Block? = null
                        var pollBlock: Block? = null

                        pollMenu?.items?.forEach {

                            val itemBlock=it.block

                            when (itemBlock?.type) {
                                BlockType.FORM_CHARTS.slug -> {
                                    chartBlock = itemBlock

                                }
                                BlockType.FORM_DISPLAY.slug -> {
                                    pollBlock = itemBlock
                                }

                            }
                        }

                        chartBlock?.let {
                            fetchChartBlock(it.slug)
                        }
                        it.copy(
                            pollBlock = pollBlock,
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

    fun fetchChartBlock(blockSlug: String) {
        // Ui state is refreshing
        _chartUiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.getBlock(SHAREDBOARDADDRESS, blockSlug)

            _chartUiState.update {
                when (result) {
                    is Result.Success -> {
                        val chartBlock = result.data.data?.block
                        it.copy(
                            chartBlock = chartBlock,
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
        fetchPollMenu(blockSlug)
    }

    fun fetchPollList(blockSlug: String, force: Boolean): Flow<PagingData<Row>> {
        Timber.e("RowsList ${blockSlug}")

        val params = ArrayMap<String, Any>()
        return repository.fetchGalley(SHAREDBOARDADDRESS, blockSlug, force, params)
            .cachedIn(viewModelScope)
    }
}
