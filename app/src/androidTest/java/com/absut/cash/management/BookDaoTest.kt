package com.absut.cash.management

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.absut.cash.management.data.db.AccountDatabase
import com.absut.cash.management.data.db.dao.BookDao
import com.absut.cash.management.data.model.Book
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var bookDao: BookDao
    private lateinit var db: AccountDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AccountDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        bookDao = db.BookDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndRetrieveBook() = runTest {
        // Given: A new book
        val book = Book(
            title = "Monthly Budget",
            bookAmount = 0,
            cashIn = 0,
            cashOut = 0
        )

        // When: Inserting the book into the database
        val bookId = bookDao.addBook(book).toInt()

        // Then: The retrieved book should match the inserted book
        val retrievedBook = bookDao.getBookById(bookId).first()
        Truth.assertThat(retrievedBook.title).isEqualTo(book.title)
        Truth.assertThat(retrievedBook.bookAmount).isEqualTo(book.bookAmount)
    }

    @Test
    fun getAllBooks() = runTest {
        // Given: Insert some books
        val book1 = Book(title = "Monthly Budget", bookAmount = 0, cashIn = 0, cashOut = 0)
        val book2 = Book(title = "Yearly Budget", bookAmount = 0, cashIn = 0, cashOut = 0)
        bookDao.addBook(book1)
        bookDao.addBook(book2)

        // When: Get all books
        val allBooks = bookDao.getAllBooks().first()

        // Then: Check if the list contains the inserted books
        //Truth.assertThat(allBooks).containsExactly(book1, book2)
        Truth.assertThat(allBooks.size).isEqualTo(2)
    }

    @Test
    fun updateBook() = runTest {
        // Given: A book inserted into the database
        val book = Book(title = "Monthly Budget", bookAmount = 0, cashIn = 0, cashOut = 0)
        val bookId = bookDao.addBook(book).toInt()

        // When: Updating the book's properties
        val updatedBook = book.copy(id = bookId, title = "Updated Budget", bookAmount = 1000)
        bookDao.updateBook(updatedBook)

        // Then: Check if the book has been updated
        val retrievedBook = bookDao.getBookById(bookId).first()
        Truth.assertThat(retrievedBook.title).isEqualTo("Updated Budget")
        Truth.assertThat(retrievedBook.bookAmount).isEqualTo(1000)
    }

    @Test
    fun deleteBook() = runTest {
        // Given: A book inserted into the database
        val book = Book(title = "Monthly Budget", bookAmount = 0, cashIn = 0, cashOut = 0)
        val bookId = bookDao.addBook(book)

        // When: Deleting the book
        bookDao.deleteBook(book)

        // Then: Check if the book has been deleted
        val allBooks = bookDao.getAllBooks().first()
        Truth.assertThat(allBooks).doesNotContain(book)
    }

}