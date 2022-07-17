package com.formaloo.home.vm


import androidx.lifecycle.viewModelScope
import com.formaloo.common.base.BaseViewModel
import com.formaloo.model.local.Speaker
import com.formaloo.repository.board.BoardRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * UI state for the SpeakerDetail screen
 */
data class SpeakerDetailUiState(
    val speakersDetail: Speaker? = null,
    val loading: Boolean = false,
) {
    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = speakersDetail == null && loading
}


class SpeakerDetailViewModel(private val repository: BoardRepo) : BaseViewModel() {

    // UI state exposed to the UI
    private val _uiDetailState = MutableStateFlow(SpeakerDetailUiState(loading = true))
    val uiDetailState: StateFlow<SpeakerDetailUiState> = _uiDetailState.asStateFlow()

    fun getSpeakerDetail(speakerSlug: String) = viewModelScope.launch {
//        _uiDetailState.update {
//            it.copy(
//                loading = true
//            )
//        }

        val speaker = withContext(Dispatchers.IO) { repository.getSpeaker(speakerSlug) }
        _uiDetailState.update {
            it.copy(
                speakersDetail = speaker,
                loading = false
            )
        }
    }
}
