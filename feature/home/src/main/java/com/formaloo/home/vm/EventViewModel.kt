package com.formaloo.home.vm

import androidx.lifecycle.viewModelScope
import com.formaloo.common.Constants.SHAREDBOARDADDRESS
import com.formaloo.common.base.BaseViewModel
import com.formaloo.model.Result
import com.formaloo.model.form.Form
import com.formaloo.repository.FormzRepo
import com.formaloo.repository.board.BoardRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * UI state for the Home screen
 */
data class EventUiState(
    val eventForm: Form? = null,
    val loading: Boolean = false,
    val errorMessages: List<String> = emptyList()
) {
    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = errorMessages.isEmpty() && loading
}


class EventViewModel(private val repository: BoardRepo, private val formRepo: FormzRepo) :
    BaseViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(EventUiState(loading = true))
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()


    fun refreshEvent(blockSlug: String) {
        // Ui state is refreshing
        _uiState.update { it.copy(loading = true) }


        viewModelScope.launch {
            repository.getBlockFromDB(blockSlug)?.let {
                fetchForm(it.form?.address ?: "")

            }

            val result = repository.getBlock(SHAREDBOARDADDRESS, blockSlug)

            when (result) {
                is Result.Success -> {
                    val block = result.data.data?.block
                    block?.let { repository.saveBlockToDB(it) }

                    fetchForm(block?.form?.address ?: "")

                }
                is Result.Error -> {
                }

            }
        }
    }

    fun fetchBlock(blockSlug: String) {
        refreshEvent(blockSlug)
    }

    fun fetchForm(formAddress: String) {
        // Ui state is refreshing
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {

             repository.getFormData(formAddress)?.let {form->
                 _uiState.update {
                     it.copy(
                         eventForm = form,
                         loading = false
                     )
                 }
             }

            val result = formRepo.displayForm(formAddress)
            _uiState.update {
                when (result) {
                    is Result.Success -> {
                        val form = result.data.data?.form
                        form?.let { it1 -> repository.saveForm(it1) }
                        it.copy(
                            eventForm = form,
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


}
