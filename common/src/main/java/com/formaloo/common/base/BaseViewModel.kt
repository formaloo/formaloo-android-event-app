package com.formaloo.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.formaloo.common.exception.Failure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure

    }

}
