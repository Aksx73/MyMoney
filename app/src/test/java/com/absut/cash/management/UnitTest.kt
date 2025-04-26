package com.absut.cash.management

import org.junit.Test

import org.junit.Assert.*

class BookCRUDUnitTest {

    @Test
    fun insertAndRetrieveBook() {
        // Given: A new book
        val book = Book(title = "Monthly Budget", bookAmount = 0, cashIn = 0, cashOut = 0)

        // When: Inserting the book into the database
        val bookId = bookDao.insert(book)
        val retrievedBook = bookDao.getBookById(bookId)

        // Then: The retrieved book should match the inserted book
        assertEquals(book.title, retrievedBook.title)
        assertEquals(book.bookAmount, retrievedBook.bookAmount)
    }

    @Test
    fun calculateBookBalance() {
        // Given: A book with cash-in and cash-out transactions
        val cashIn = 1000
        val cashOut = 400

        // When: Calculating the balance
        val balance = bookViewModel.calculateBalance(cashIn, cashOut)

        // Then: The balance should be correctly calculated
        assertEquals(600, balance)
    }

}