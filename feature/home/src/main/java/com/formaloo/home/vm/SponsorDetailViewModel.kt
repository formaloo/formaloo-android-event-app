package com.formaloo.home.vm


import androidx.lifecycle.viewModelScope
import com.formaloo.common.base.BaseViewModel
import com.formaloo.model.local.Sponsor
import com.formaloo.repository.board.BoardRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * UI state for the SponsorDetail screen
 */
data class SponsorDetailUiState(
    val sponsorsDetail: Sponsor? = null,
    val loading: Boolean = false,
) {
    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = sponsorsDetail == null && loading
}


class SponsorDetailViewModel(private val repository: BoardRepo) : BaseViewModel() {

    // UI state exposed to the UI
    private val _uiDetailState = MutableStateFlow(SponsorDetailUiState(loading = true))
    val uiDetailState: StateFlow<SponsorDetailUiState> = _uiDetailState.asStateFlow()

    fun getSponsorDetail(sponsorSlug: String) = viewModelScope.launch {
//        _uiDetailState.update {
//            it.copy(
//                loading = true
//            )
//        }

        val sponsor = withContext(Dispatchers.IO) { repository.getSponsor(sponsorSlug) }
        _uiDetailState.update {
            it.copy(
                sponsorsDetail = sponsor,
                loading = false
            )
        }
    }
}
