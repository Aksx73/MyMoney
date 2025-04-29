package com.absut.cash.management.data.repository

import com.absut.cash.management.data.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun addCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    fun getAllCategories(): Flow<List<Category>>
    fun getActiveCategories(): Flow<List<Category>>
    fun getInactiveCategories(): Flow<List<Category>>
}