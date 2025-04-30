package com.absut.cash.management.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.absut.cash.management.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constants.CATEGORY_TABLE_NAME)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iconId: Int? = 0,
    val isActive: Boolean = true
) : Parcelable

/**List of default categories to be added to the database*/
object DefaultCategories {
    val defaults = listOf(
        Category(name = "Food & Drinks", iconId = 5, isActive = true),
        Category(name = "Groceries", iconId = 41, isActive = true),
        Category(name = "Shopping", iconId = 20, isActive = true),
        Category(name = "Commute", iconId = 35, isActive = true),
        Category(name = "Housing & Bills", iconId = 41, isActive = true),
        Category(name = "Education", iconId = 18, isActive = true),
        Category(name = "Sports & Games", iconId = 42, isActive = true),
        Category(name = "Travel", iconId = 24, isActive = true),
        Category(name = "Gift", iconId = 43, isActive = true),
        Category(name = "Family Care", iconId = 44, isActive = true),
        Category(name = "Self Transfer", iconId = 46, isActive = true),
        Category(name = "Money Transfer", iconId = 46, isActive = true),
        Category(name = "Income", iconId = 45, isActive = true),
        Category(name = "Personal Care", iconId = 47, isActive = true),
        Category(name = "Medical", iconId = 32, isActive = true),
        Category(name = "Business Spends", iconId = 29, isActive = true),
        Category(name = "Miscellaneous", iconId = 48, isActive = true),
    )
}
