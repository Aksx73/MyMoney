package com.absut.cash.management.data.repository

import com.absut.cash.management.data.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun addCategory(category: Category)

    fun getAllCategories(bookId: Int): Flow<List<Category>>
}