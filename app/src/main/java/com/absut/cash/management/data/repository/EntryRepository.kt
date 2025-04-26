package com.absut.cash.management.data.repository

import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.model.EntryWithCategory
import kotlinx.coroutines.flow.Flow

interface EntryRepository {

    suspend fun addEntry(entry: Entry)

    fun getAllEntries(bookId: Int) : Flow<List<Entry>>

    fun getAllEntriesWithCategory(bookId: Int) : Flow<List<EntryWithCategory>>

    fun getBookDetails(bookId: Int) : Flow<Book>

    suspend fun updateEntry(entry: Entry)

    suspend fun deleteEntry(entry: Entry)

    suspend fun deleteAllEntries(bookId: Int)

    fun getCategories(): Flow<List<Category>>
}