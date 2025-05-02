package com.absut.cash.management

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.repository.BookRepository
import com.absut.cash.management.data.repository.EntryRepository
import com.absut.cash.management.ui.entrylist.EntryListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class EntryListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockBookRepository = Mockito.mock(BookRepository::class.java)
    private val mockEntryRepository = Mockito.mock(EntryRepository::class.java)
    private val entryListViewModel = EntryListViewModel(mockEntryRepository,mockBookRepository)

    @Test
    fun addCashEntry_updatesBookAmounts() = runTest {
        // Given: A mock book with initial amounts
        val initialBook = Book(title = "Test Book", bookAmount = 1000, cashIn = 1000, cashOut = 0)
        Mockito.`when`(mockEntryRepository.getBookDetails(1)).thenReturn(flowOf(initialBook))

        // When: Adding a cash-in entry
        val cashInAmount = 500
        val entry = Entry(id = 0, entryAmount = cashInAmount, entryType = 0, bookId = 1, categoryId = null, updatedAt = System.currentTimeMillis(), description = "")
        entryListViewModel.addEntry(entry)

        // Verify: The repository updates the book amounts
        val updatedBook = initialBook.copy(
            cashIn = initialBook.cashIn + cashInAmount,
            bookAmount = initialBook.bookAmount + cashInAmount,
        )
        Mockito.verify(mockBookRepository).updateBook(updatedBook)

        // Then: Ensure LiveData/StateFlow reflects the updated amounts
        assertEquals(updatedBook.bookAmount, entryListViewModel.book.value?.bookAmount)
    }
}