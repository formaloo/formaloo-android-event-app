package com.formaloo.remote

import com.formaloo.model.form.formDetail.FormDetailRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FormService {
    companion object {
        private const val displayForm = "v3.1/form-displays/address/{address}/"
    }

    @GET(displayForm)
    fun displayForm(
        @Path("address") slug: String
    ): Call<FormDetailRes>

}

