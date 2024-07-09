package com.example.fundsense

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MainTransactions::class), version = 1)
abstract class MainDataBase : RoomDatabase() {
    abstract fun dataAccessObj(): dataAccessObj
}