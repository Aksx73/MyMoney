package com.absut.cash.management.ui.entrydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryDetailViewModel @Inject constructor(
    private val repository: EntryRepository
) : ViewModel() {

    private val mutableResponseMessage = MutableLiveData("")
    val responseMessage: LiveData<String> get() = mutableResponseMessage

    private val mutableActionType = MutableLiveData(0)
    val actionType: LiveData<Int> get() = mutableActionType

    val defaultCategory = Category(0, "None", 0)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        getCategories()
    }

    fun addEntry(entry: Entry) {
        viewModelScope.launch {
            repository.addEntry(entry)
            mutableActionType.value = 1
            mutableResponseMessage.value = "Entry added."
        }
    }

    fun updateEntry(entry: Entry) {
        viewModelScope.launch {
            repository.updateEntry(entry)
            mutableActionType.value = 2
            mutableResponseMessage.value = "Entry updated."
        }
    }

    fun getCategories() = viewModelScope.launch {
        repository.getCategories()
            .collect { categories ->
                _categories.value = listOf(defaultCategory) + categories
            }
    }

}