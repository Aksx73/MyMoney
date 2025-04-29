package com.absut.cash.management.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.util.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category_table ORDER BY id ASC")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category_table WHERE isActive = 1 ORDER BY id ASC")
    fun getActiveCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category_table WHERE isActive = 0 ORDER BY id ASC")
    fun getInactiveCategories(): Flow<List<Category>>

    @Update
    suspend fun update(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(category: Category)

    @Delete
    suspend fun delete(category: Category)
}