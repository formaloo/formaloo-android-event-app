package com.formaloo.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.formaloo.model.form.Form

@Dao
abstract class FormDao : AppUIBaseDao<Form>() {

    @Query("SELECT * FROM Form ")
    abstract fun getForms(): PagingSource<Int, Form>

    @Query("SELECT * FROM Form ")
    abstract fun getFormList(): List<Form>

    @Query("SELECT * FROM Form WHERE slug = :slug")
    abstract suspend fun getForm(slug: String): Form

    @Query("DELETE FROM Form WHERE slug = :slug")
    abstract suspend fun deleteForm(slug: String)

    // ---
    @Query("DELETE FROM Form")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(forms: Form) {
        insert(forms)
    }

    suspend fun save(forms: List<Form>) {
        insert(forms)
    }

}
