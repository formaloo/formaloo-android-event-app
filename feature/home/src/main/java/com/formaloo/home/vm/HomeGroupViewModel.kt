package com.formaloo.home.vm


import androidx.lifecycle.viewModelScope
import com.formaloo.common.Constants.SHAREDBOARDADDRESS
import com.formaloo.common.base.BaseViewModel
import com.formaloo.model.Result
import com.formaloo.model.boards.block.Block
import com.formaloo.repository.board.BoardRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class HomeGroupViewModel(private val repository: BoardRepo) : BaseViewModel() {
    // Holds our currently selected home category
    private val selectedCategory = MutableStateFlow<Block?>(null)
    private val categories = MutableStateFlow(arrayListOf<Block>())

    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(HomeViewState())

    private val refreshing = MutableStateFlow(false)

    val state: StateFlow<HomeViewState>
        get() = _state

    fun onHomeCategorySelected(category: Block) {
        selectedCategory.value = category
    }


    fun refreshEvent(blockSlug: String) {
        // Ui state is refreshing
        _state.update { it.copy(refreshing = true) }

        viewModelScope.launch {

            repository.getBlockFromDB(blockSlug)?.let {
                blockIsReadey(it)
            }

            val result = repository.getBlock(SHAREDBOARDADDRESS, blockSlug)

            when (result) {
                is Result.Success -> {
                    val block = result.data.data?.block

                    block?.let {
                        repository.saveBlockToDB(block)
                    }

                    blockIsReadey(block)


                }

            }

        }
    }

    private fun blockIsReadey(block: Block?) =viewModelScope.launch{
        val items = block?.items
        val selectedCat: Block? = items?.let {
            it[0]
        }
        selectedCat?.let {
            onHomeCategorySelected(selectedCat)

        }
        categories.value = items ?: arrayListOf()

        combine(
            categories,
            selectedCategory,
            refreshing
        ) { categories, selectedCategory, refreshing ->
            HomeViewState(
                homeCategories = categories,
                selectedHomeCategory = selectedCategory,
                refreshing = refreshing,
                errorMessage = null /* TODO */
            )
        }.catch { throwable ->
            // TODO: emit a UI error here. For now we'll just rethrow
            throw throwable
        }.collect {
            _state.value = it
        }

    }

    fun fetchBlock(blockSlug: String) {
        refreshEvent(blockSlug)
    }
}


data class HomeViewState(
    val homeBlocks: List<Block> = emptyList(),
    val refreshing: Boolean = false,
    val selectedHomeCategory: Block? = null,
    val homeCategories: List<Block> = emptyList(),
    val errorMessage: String? = null
) {
    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = homeBlocks.isEmpty() && homeCategories.isEmpty() && errorMessage?.isEmpty() ?: true && refreshing
}



