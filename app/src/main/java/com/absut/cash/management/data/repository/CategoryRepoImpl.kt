package com.absut.cash.management.data.repository

import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.db.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(private val dao: CategoryDao): CategoryRepository {
    override suspend fun addCategory(category: Category) {
        return dao.add(category)
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return dao.getCategories()
    }

    override fun getActiveCategories(): Flow<List<Category>> {
        return dao.getActiveCategories()
    }

    override fun getInactiveCategories(): Flow<List<Category>> {
        return dao.getInactiveCategories()
    }

    override suspend fun updateCategory(category: Category) {
        return dao.update(category)
    }

    override suspend fun deleteCategory(category: Category) {
        return dao.delete(category)
    }
}