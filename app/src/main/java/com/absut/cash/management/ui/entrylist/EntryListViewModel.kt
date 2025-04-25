package com.absut.cash.management.ui.entrylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.model.EntryWithCategory
import com.absut.cash.management.data.model.EventWrapper
import com.absut.cash.management.data.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryListViewModel @Inject constructor(
    private val repository: EntryRepository
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book = _book.asStateFlow()

    private val _entries = MutableStateFlow<List<EntryWithCategory>>(emptyList())
    val entries = _entries.asStateFlow()

    private val _uiMessage = MutableSharedFlow<String?>()
    val uiMessage = _uiMessage.asSharedFlow()

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
            _uiMessage.emit("Entry deleted successfully")
        } catch (e: Exception) {
            _uiMessage.emit("Failed to delete entry")
        }
    }

    fun deleteAllEntries(bookId: Int) = viewModelScope.launch {
        try {
            repository.deleteAllEntries(bookId)
            _uiMessage.emit("All entries deleted successfully")
        } catch (e: Exception) {
            _uiMessage.emit("Failed to delete entries")
        }
    }

}