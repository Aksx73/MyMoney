package com.absut.cash.management.data.repository

import com.absut.cash.management.data.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun getAllBooks(): Flow<List<Book>>

    suspend fun addBook(book: Book)

    suspend fun updateBook(book: Book)

    suspend fun updateBookTitle(bookId: Int, title: String)

    suspend fun deleteBook(book: Book)
}