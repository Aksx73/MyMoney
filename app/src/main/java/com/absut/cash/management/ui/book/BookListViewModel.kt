package com.absut.cash.management.ui.book

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books = _books.asStateFlow()

    private val _uiMessage = MutableSharedFlow<String?>()
    val uiMessage = _uiMessage.asSharedFlow()

    val isLoading = mutableStateOf(false)

    var selectedBook: Book? = null

    fun clearUiMessage() = viewModelScope.launch {
        _uiMessage.emit(null)
    }

    fun addBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingCategory = _books.value.find {
                    it.title.equals(book.title, ignoreCase = true)
                }

                if (existingCategory != null) {
                    _uiMessage.emit("Book with this name already exists")
                    return@launch
                }

                repository.addBook(book)
                _uiMessage.emit("Book added successfully")

            } catch (e: Exception) {
                _uiMessage.emit("Failed to add book")
            }
        }
    }

    fun getAllBooks() = viewModelScope.launch(Dispatchers.IO) {
        isLoading.value = true
        repository.getAllBooks()
            .collect { books ->
                _books.value = books
                isLoading.value = false
            }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            try {
                repository.updateBook(book)
                _uiMessage.emit("Book updated successfully")
            } catch (e: Exception) {
                _uiMessage.emit("Failed to update book")
            }
        }
    }

    fun updateBookTitle(bookId: Int, title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateBookTitle(bookId, title)
                _uiMessage.emit("Book renamed successfully")
            } catch (e: Exception) {
                _uiMessage.emit("Failed to rename book")
            }
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteBook(book)
                _uiMessage.emit("Book deleted successfully")
            } catch (e: Exception) {
                _uiMessage.emit("Failed to delete book")
            }
        }
    }

}