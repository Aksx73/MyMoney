package com.absut.cash.management.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.absut.cash.management.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constants.BOOKS_TABLE_NAME)
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var bookAmount: Int = 0, //1000
    var cashIn: Int = 0,    //  1200+100
    val cashOut: Int = 0,   //   200
    val created: Long = System.currentTimeMillis()
) : Parcelable
