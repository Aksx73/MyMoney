package com.absut.cash.management.ui.entrylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.model.EventWrapper
import com.absut.cash.management.data.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryListViewModel @Inject constructor(
    private val repository: EntryRepository
): ViewModel() {

    private val mutableEntriesList = MutableLiveData<Flow<List<Entry>>>()
    val entriesList: LiveData<Flow<List<Entry>>> get() = mutableEntriesList

    private val mutableResponseMessage = MutableLiveData<EventWrapper<String>>()
    val responseMessage: LiveData<EventWrapper<String>> get() = mutableResponseMessage

    fun getEntriesOfBook(bookId: Int) {
        mutableEntriesList.value = repository.getAllEntries(bookId)
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
            mutableResponseMessage.value = EventWrapper("Deleted")
        }
    }
}