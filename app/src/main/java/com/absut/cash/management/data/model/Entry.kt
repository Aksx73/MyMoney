package com.absut.cash.management.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.absut.cash.management.util.Constants
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Parcelize
@Entity(
    tableName = Constants.ENTRY_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("bookId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val entryAmount: Int,
    val entryType: Int, // 0 -> cashIn ; 1 -> cashOut
    val description: String?,
    @ColumnInfo(name = "bookId", index = true)
    val bookId: Int,
    @ColumnInfo(name = "categoryId", index = true)
    val categoryId: Int?,
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable {
    val formattedUpdatedAt: String
        get() = DateFormat.getDateInstance().format(updatedAt)
}

data class EntryWithCategory(
    @Embedded val entry: Entry,
    @Embedded(prefix = "category_") val category: Category?
)
