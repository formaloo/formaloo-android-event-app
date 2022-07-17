package com.formaloo.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy

abstract class AppUIBaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insert(items: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insert(item: T)
}
