package com.absut.cash.management.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.util.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM "+ Constants.CATEGORY_TABLE_NAME + " WHERE book_id=:bookId")
    fun getCategoriesOfBook(bookId: Int): Flow<List<Category>>

    @Insert
    suspend fun addCategory(category: Category)
}