package com.absut.cash.management.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.absut.cash.management.util.Constants
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = Constants.BOOKS_TABLE_NAME)
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var bookAmount: Int = 0,
    var cashIn: Int = 0,
    val cashOut: Int = 0,
    val created: Long = System.currentTimeMillis()
) : Parcelable
