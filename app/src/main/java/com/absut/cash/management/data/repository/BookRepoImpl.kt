package com.absut.cash.management.data.repository

import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.db.dao.BookDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepoImpl @Inject constructor(private val dao: BookDao) : BookRepository {

    override fun getAllBooks(): Flow<List<Book>> {
        return dao.getAllBooks()
    }

    override suspend fun addBook(book: Book) {
        dao.addBook(book)
    }

    override suspend fun updateBook(book: Book) {
        dao.updateBook(book)
    }

    override suspend fun updateBookTitle(bookId: Int, title: String) {
        dao.updateBookTitle(bookId, title)
    }

    override suspend fun deleteBook(book: Book) {
        /**
         * only delete the book as we already have added rules to delete all entries if associated book is deleted
         * */
        dao.deleteBook(book)
    }

}