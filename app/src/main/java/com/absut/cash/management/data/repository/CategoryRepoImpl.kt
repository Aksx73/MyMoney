package com.absut.cash.management.data.repository

import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.db.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(private val dao: CategoryDao): CategoryRepository {
    override suspend fun addCategory(category: Category) {
        return dao.addCategory(category)
    }

    override fun getAllCategories(bookId: Int): Flow<List<Category>> {
        return dao.getCategoriesOfBook(bookId)
    }
}