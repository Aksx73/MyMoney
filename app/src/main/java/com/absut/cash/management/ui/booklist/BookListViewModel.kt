package com.absut.cash.management.ui.booklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.EventWrapper
import com.absut.cash.management.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    
    private val repository: BookRepository
) : ViewModel() {

    private val mutableBook = MutableLiveData<Book?>()
    val currentBook: LiveData<Book?> get() = mutableBook

    private val mutableResponseMessage = MutableLiveData<EventWrapper<String>>()
    val responseMessage: LiveData<EventWrapper<String>> get() = mutableResponseMessage

    fun setCurrentBook(book: Book?) {
        mutableBook.value = book
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            repository.addBook(book)
            mutableResponseMessage.value = EventWrapper("Book added.")
        }
    }

    fun getAllBooks(): Flow<List<Book>> = repository.getAllBooks()

    fun updateBook(book: Book) {
        mutableBook.value = book
        viewModelScope.launch {
            repository.updateBook(book)
        }
    }

    fun updateBookTitle(bookId: Int, title: String) {
        viewModelScope.launch {
            repository.updateBookTitle(bookId, title)
            mutableResponseMessage.value = EventWrapper("Book title updated.")
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }

}