package com.absut.cash.management.data.repository

import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.Entry
import kotlinx.coroutines.flow.Flow

interface EntryRepository {

    suspend fun addEntry(entry: Entry)

    fun getAllEntries(bookId: Int) : Flow<List<Entry>>

    fun getBookDetails(bookId: Int) : Book

    suspend fun updateEntry(entry: Entry)

    suspend fun deleteEntry(entry: Entry)
}