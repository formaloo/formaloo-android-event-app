package com.formaloo.home.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.formaloo.common.base.BaseViewModel
import com.formaloo.model.boards.board.Board
import com.formaloo.model.boards.board.detail.BoardDetailRes
import com.formaloo.repository.board.BoardRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BoardViewModel(private val repository: BoardRepo) : BaseViewModel() {

    private val _board = MutableLiveData<Board>().apply { value = null }
    val board: LiveData<Board> = _board

    fun retrieveSharedBoardDetail(sharedBoardAddress: String) = viewModelScope.launch {
        _board.value = withContext(Dispatchers.IO) { repository.getBoardData(sharedBoardAddress) }

        val result =
            withContext(Dispatchers.IO) { repository.getSharedBoardDetail(sharedBoardAddress) }
        result.either(::handleFailure, ::handleBoardDetailData)
    }


    private fun handleBoardDetailData(res: BoardDetailRes?) {
        res?.let {
            it.data?.let {
                it.board?.let {
                    saveBoard(it)
                    _board.value = it

                }


            }
        }
    }

    private fun saveBoard(board: Board) = viewModelScope.launch {
        repository.saveBoard(board)


    }


}
