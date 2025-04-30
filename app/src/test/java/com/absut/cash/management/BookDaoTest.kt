package com.absut.cash.management

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.absut.cash.management.data.db.AccountDatabase
import com.absut.cash.management.data.db.dao.BookDao
import com.absut.cash.management.data.model.Book
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first

import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

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
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
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
        assertThat(retrievedBook.title).isEqualTo(book.title)
        assertThat(retrievedBook.bookAmount).isEqualTo(book.bookAmount)
    }

    @Test
    @Throws(Exception::class)
    fun getAllBooks() = runTest {
        // Given: Insert some books
        val book1 = Book(title = "Monthly Budget", bookAmount = 0, cashIn = 0, cashOut = 0)
        val book2 = Book(title = "Yearly Budget", bookAmount = 0, cashIn = 0, cashOut = 0)
        bookDao.addBook(book1)
        bookDao.addBook(book2)

        // When: Get all books
        val allBooks = bookDao.getAllBooks().first()

        // Then: Check if the list contains the inserted books
        assertThat(allBooks).containsExactly(book1, book2)
    }

    @Test
    @Throws(Exception::class)
    fun updateBook() = runTest {
        // Given: A book inserted into the database
        val book = Book(title = "Monthly Budget", bookAmount = 0, cashIn = 0, cashOut = 0)
        val bookId = bookDao.addBook(book).toInt()

        // When: Updating the book's properties
        val updatedBook = book.copy(id = bookId, title = "Updated Budget", bookAmount = 1000)
        bookDao.updateBook(updatedBook)

        // Then: Check if the book has been updated
        val retrievedBook = bookDao.getBookById(bookId).first()
        assertThat(retrievedBook.title).isEqualTo("Updated Budget")
        assertThat(retrievedBook.bookAmount).isEqualTo(1000)
    }

    @Test
    @Throws(Exception::class)
    fun deleteBook() = runTest {
        // Given: A book inserted into the database
        val book = Book(title = "Monthly Budget", bookAmount = 0, cashIn = 0, cashOut = 0)
        val bookId = bookDao.addBook(book)

        // When: Deleting the book
        bookDao.deleteBook(book)

        // Then: Check if the book has been deleted
        val allBooks = bookDao.getAllBooks().first()
        assertThat(allBooks).isEmpty()
    }

    /*@Test
    fun calculateBookBalance() {
        // Given: A book with cash-in and cash-out transactions
        val cashIn = 1000
        val cashOut = 400

        // When: Calculating the balance
        val balance = bookViewModel.calculateBalance(cashIn, cashOut)

        // Then: The balance should be correctly calculated
        assertEquals(600, balance)
    }*/


}