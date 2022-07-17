package com.formaloo.repository

import com.formaloo.model.Result
import com.formaloo.model.form.formDetail.FormDetailRes
import com.formaloo.remote.FormAllDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FormzRepoImpl(
    val source: FormAllDatasource,
) : FormzRepo, BaseRepo() {


    override suspend fun displayForm(formAddress: String): Result<FormDetailRes> {
        return withContext(Dispatchers.IO) {
            val call = source.displayForm(formAddress)
            try {
                callRequest(call, { it.toFormDetailRes() }, FormDetailRes.empty())
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }


}
