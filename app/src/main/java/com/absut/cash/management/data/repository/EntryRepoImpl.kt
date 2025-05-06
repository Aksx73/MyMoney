package com.absut.cash.management.data.repository

import com.absut.cash.management.data.db.dao.BookDao
import com.absut.cash.management.data.db.dao.CategoryDao
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.db.dao.EntryDao
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.model.EntryWithCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EntryRepoImpl @Inject constructor(
    private val dao: EntryDao,
    private val bookDao: BookDao,
    private val categoryDao: CategoryDao
): EntryRepository {
    override suspend fun addEntry(entry: Entry) {
        return dao.addEntry(entry)
    }

    override fun getBookDetails(bookId: Int): Flow<Book?> {
        return bookDao.getBookById(bookId)
    }

    override suspend fun updateEntry(entry: Entry) {
        return dao.updateEntry(entry)
    }

    override suspend fun deleteEntry(entry: Entry) {
        return dao.deleteEntry(entry)
    }

    override fun getAllEntries(bookId: Int): Flow<List<Entry>> {
        return dao.getAllEntriesOfBook(bookId)
    }

    override fun getAllEntriesWithCategory(bookId: Int): Flow<List<EntryWithCategory>> {
        return dao.getEntriesWithCategory(bookId)
    }

    override suspend fun deleteAllEntries(bookId: Int) {
        return dao.deleteAllEntriesOfBook(bookId)
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getCategories()
    }
}