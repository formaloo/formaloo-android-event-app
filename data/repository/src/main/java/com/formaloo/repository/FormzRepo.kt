package com.formaloo.repository

import com.formaloo.model.Result
import com.formaloo.model.form.formDetail.FormDetailRes

interface FormzRepo {

    suspend fun displayForm(formAddress: String): Result<FormDetailRes>

}
