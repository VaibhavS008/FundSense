package com.example.fundsense

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface dataAccessObj {
    @Query("SELECT * from All_Transaction ")
    fun getAll():List<MainTransactions>

    @Insert
    fun insertAll(vararg transaction: MainTransactions)

    @Delete
    fun delete(transaction: MainTransactions)

    @Update
    fun update(vararg transaction: MainTransactions)
}