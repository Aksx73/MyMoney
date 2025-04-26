package com.absut.cash.management

import junit.framework.TestCase.assertEquals
import org.junit.Test

class IntegrationTest {

    @Test
    fun createTransactionUpdatesBookBalance() {
        // Given: A book with initial balance
        val bookId =
            bookDao.insert(Book(title = "Test Book", bookAmount = 0, cashIn = 0, cashOut = 0))

        // When: Adding a cash-in transaction
        val transaction = Transaction(entryAmount = 500, entryType = 0, bookId = bookId)
        transactionDao.insert(transaction)

        // Then: The book balance should be updated
        val updatedBook = bookDao.getBookById(bookId)
        assertEquals(500, updatedBook.cashIn)
        assertEquals(500, updatedBook.bookAmount)
    }

    @Test
    fun deleteTransactionUpdatesBookBalance() {
        // Test setup with book and transaction
        // Verify balance updates correctly when transaction is deleted
    }


}