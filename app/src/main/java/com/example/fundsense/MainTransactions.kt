package com.example.fundsense

import android.icu.text.SimpleDateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date
import java.util.Locale

@Entity(tableName = "All_Transaction")
data class MainTransactions(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val label: String,
    val data: Double,
    val date: Long
) : Serializable {

    fun formattedDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date(date))
    }

    fun formattedAmount(): String {
        val amountFormatted = if (data >= 0) {
            "+ ₹%.2f".format(data)
        } else {
            "- ₹%.2f".format(Math.abs(data))
        }
        return amountFormatted
    }
}
