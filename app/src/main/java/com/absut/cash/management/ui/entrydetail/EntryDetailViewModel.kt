package com.absut.cash.management.ui.entrydetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.compareTo

@HiltViewModel
class EntryDetailViewModel @Inject constructor(
    private val repository: EntryRepository
) : ViewModel() {

    private val _uiMessage = MutableSharedFlow<String?>()
    val uiMessage = _uiMessage.asSharedFlow()

    private val _entryAddUpdateSuccess = mutableStateOf(false)
    val entryAddUpdateSuccess: Boolean get() = _entryAddUpdateSuccess.value

    private val mutableActionType = MutableLiveData(0)
    val actionType: LiveData<Int> get() = mutableActionType

    val defaultCategory = Category(0, "None", 0)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        getCategories()
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
                _entryAddUpdateSuccess.value = true
                _uiMessage.emit("Entry added successfully")
            } catch (e: Exception) {
                _entryAddUpdateSuccess.value = false
                _uiMessage.emit("Failed to add entry")
            }
            //mutableActionType.value = 1
        }
    }

    fun updateEntry(entry: Entry) = viewModelScope.launch {
        try {
            repository.updateEntry(entry)
            _entryAddUpdateSuccess.value = true
            _uiMessage.emit("Entry updated successfully")
        } catch (e: Exception) {
            _entryAddUpdateSuccess.value = false
            _uiMessage.emit("Failed to update entry")
        }
    }

    fun getCategories() = viewModelScope.launch {
        repository.getCategories()
            .collect { categories ->
                _categories.value = listOf(defaultCategory) + categories
            }
    }

}