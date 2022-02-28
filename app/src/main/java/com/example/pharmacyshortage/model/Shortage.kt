package com.example.pharmacyshortage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

/**
 * Shortage entity to be stored into shortage_table
 */
@Entity(tableName = "shortage_table")
data class Shortage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shortage_id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "previous_price")
    val previousPrice: Int,
    @ColumnInfo(name = "type")
    val type: List<String?>,
    @ColumnInfo(name = "important")
    val important: Boolean
)
fun Shortage.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(previousPrice)