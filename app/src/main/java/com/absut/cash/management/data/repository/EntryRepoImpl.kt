package com.absut.cash.management.data.repository

import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.db.dao.EntryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EntryRepoImpl @Inject constructor(private val dao: EntryDao): EntryRepository {
    override suspend fun addEntry(entry: Entry) {
        return dao.addEntry(entry)
    }

    override fun getBookDetails(bookId: Int): Book {
        TODO("Not yet implemented")
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
}