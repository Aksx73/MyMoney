package com.absut.cash.management.ui.entrylist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.model.EntryWithCategory
import com.absut.cash.management.data.repository.BookRepository
import com.absut.cash.management.data.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryListViewModel @Inject constructor(
    private val repository: EntryRepository,
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book = _book.asStateFlow()

    private val _entries = MutableStateFlow<List<EntryWithCategory>>(emptyList())
    val entries = _entries.asStateFlow()

    private val _uiMessage = MutableSharedFlow<String?>()
    val uiMessage = _uiMessage.asSharedFlow()

    private val _entryAddUpdateSuccess = mutableStateOf(false)
    val entryAddUpdateSuccess: Boolean get() = _entryAddUpdateSuccess.value

    fun resetEntryAddUpdateSuccess() {
        _entryAddUpdateSuccess.value = false
    }

    var selectedEntry: EntryWithCategory? = null

    fun getBookFromId(bookId: Int) = viewModelScope.launch {
        repository.getBookDetails(bookId)
            .collect { book ->
                _book.value = book
            }
    }

    /*fun getEntriesOfBook(bookId: Int) = viewModelScope.launch {
        repository.getAllEntries(bookId)
            .collect { entries ->
                _entries.value = entries
            }
    }*/

    fun getEntriesOfBook(bookId: Int) = viewModelScope.launch {
        repository.getAllEntriesWithCategory(bookId)
            .collect { entries ->
                _entries.value = entries
            }
    }

    fun deleteEntry(entry: Entry) = viewModelScope.launch {
        try {
            repository.deleteEntry(entry)
            updateBooKAmountOnEntryDelete(entry)
            //_uiMessage.emit("Entry deleted successfully")
        } catch (e: Exception) {
            _uiMessage.emit("Failed to delete entry")
        }
    }

    fun deleteAllEntries(bookId: Int) = viewModelScope.launch {
        try {
            repository.deleteAllEntries(bookId)
            updateBooKAmountOnAllEntryDelete()
        } catch (e: Exception) {
            _uiMessage.emit("Failed to delete entries")
        }
    }

    fun clearUiMessage() = viewModelScope.launch {
        _uiMessage.emit(null)
    }

    fun addEntry(entry: Entry) {
        viewModelScope.launch {
            try {
                // Validate foreign keys before inserting
                if (entry.bookId <= 0) {
                    _uiMessage.emit("Invalid book reference")
                    return@launch
                }
                repository.addEntry(entry)
                updateBookAmounts(entry)
            } catch (e: Exception) {
                _entryAddUpdateSuccess.value = false
                _uiMessage.emit("Failed to add entry")
            }
        }
    }

    fun updateEntry(entry: Entry) = viewModelScope.launch {
        try {
            repository.updateEntry(entry)
            updateBookAmountsForEdit(selectedEntry?.entry!!, entry)
        } catch (e: Exception) {
             _entryAddUpdateSuccess.value = false
            _uiMessage.emit("Failed to update entry")
        }
    }


    val defaultCategory = Category(0, "None", 0)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()
    fun getCategories() = viewModelScope.launch {
        repository.getCategories()
            .collect { categories ->
                _categories.value = listOf(defaultCategory) + categories
            }
    }


    private fun updateBookAmounts(entry: Entry) {
        _book.value?.let { currentBook ->
            val updatedBook = when (entry.entryType) {
                EntryType.CASH_IN.value -> currentBook.copy(
                    cashIn = currentBook.cashIn.plus(entry.entryAmount)
                )

                EntryType.CASH_OUT.value -> currentBook.copy(
                    cashOut = currentBook.cashOut.plus(entry.entryAmount)
                )

                else -> currentBook
            }
            updatedBook.bookAmount = updatedBook.cashIn.minus(updatedBook.cashOut)

            viewModelScope.launch {
                try {
                    bookRepository.updateBook(updatedBook)
                    _entryAddUpdateSuccess.value = true
                } catch (e: Exception) {
                    _entryAddUpdateSuccess.value = false
                    _uiMessage.emit("Failed to update book")
                }
            }
        }
    }

    private fun updateBookAmountsForEdit(oldEntry: Entry, newEntry: Entry) {
        _book.value?.let { currentBook ->
            var updatedBook = currentBook

            // First, reverse the old entry's effect
            updatedBook = when (oldEntry.entryType) {
                EntryType.CASH_IN.value -> updatedBook.copy(
                    cashIn = updatedBook.cashIn.minus(oldEntry.entryAmount)
                )

                EntryType.CASH_OUT.value -> updatedBook.copy(
                    cashOut = updatedBook.cashOut.minus(oldEntry.entryAmount)
                )

                else -> updatedBook
            }

            // Then, add the new entry's effect
            updatedBook = when (newEntry.entryType) {
                EntryType.CASH_IN.value -> updatedBook.copy(
                    cashIn = updatedBook.cashIn.plus(newEntry.entryAmount)
                )

                EntryType.CASH_OUT.value -> updatedBook.copy(
                    cashOut = updatedBook.cashOut.plus(newEntry.entryAmount)
                )

                else -> updatedBook
            }

            // Update final book amount
            updatedBook.bookAmount = updatedBook.cashIn - updatedBook.cashOut

            viewModelScope.launch {
                try {
                    bookRepository.updateBook(updatedBook)
                    _entryAddUpdateSuccess.value = true
                    //_uiMessage.emit("Entry updated successfully")
                } catch (e: Exception) {
                    _entryAddUpdateSuccess.value = false
                    _uiMessage.emit("Failed to update book")
                }
            }
        }
    }

    private fun updateBooKAmountOnEntryDelete(entry: Entry) {
        _book.value?.let { currentBook ->
            val updatedBook = when (entry.entryType) {
                EntryType.CASH_IN.value -> currentBook.copy(
                    cashIn = currentBook.cashIn.minus(entry.entryAmount)
                )

                EntryType.CASH_OUT.value -> currentBook.copy(
                    cashOut = currentBook.cashOut.minus(entry.entryAmount)
                )

                else -> currentBook
            }
            updatedBook.bookAmount = updatedBook.cashIn.minus(updatedBook.cashOut)

            viewModelScope.launch {
                try {
                    bookRepository.updateBook(updatedBook)
                    _uiMessage.emit("Entry deleted successfully")
                } catch (e: Exception) {
                    _uiMessage.emit("Failed to update book amounts")
                }
            }
        }
    }

    private fun updateBooKAmountOnAllEntryDelete() {
        _book.value?.let { currentBook ->
            val updatedBook = currentBook.copy(
                cashIn = 0,
                cashOut = 0,
                bookAmount = 0
            )
            viewModelScope.launch {
                try {
                    bookRepository.updateBook(updatedBook)
                    _uiMessage.emit("All entries deleted successfully")
                } catch (e: Exception) {
                    _uiMessage.emit("Failed to update book amounts")
                }
            }
        }
    }

}