package com.absut.cash.management.ui.entrydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryDetailViewModel @Inject constructor(
    private val repository: EntryRepository
): ViewModel() {

    private val mutableResponseMessage = MutableLiveData("")
    val responseMessage: LiveData<String> get() = mutableResponseMessage

    private val mutableActionType = MutableLiveData(0)
    val actionType: LiveData<Int> get() = mutableActionType

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

}