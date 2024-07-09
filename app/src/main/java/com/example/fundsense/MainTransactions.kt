package com.example.fundsense

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "All_Transaction")
data class MainTransactions(
    @PrimaryKey(autoGenerate = true)val id:Int ,
    val label:String,
    val data:Double
    ): Serializable {

    }