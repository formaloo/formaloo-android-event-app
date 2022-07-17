package com.formaloo.remote


/**
 * Implementation of [FormService] interface
 */

class FormAllDatasource(private val service: FormService) {
    fun displayForm(
        formAddress: String
    ) = service.displayForm(formAddress)


}
