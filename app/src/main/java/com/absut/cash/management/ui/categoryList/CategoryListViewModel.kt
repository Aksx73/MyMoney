package com.absut.cash.management.ui.categoryList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _uiMessage = MutableSharedFlow<String?>()
    val uiMessage = _uiMessage.asSharedFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    var selectedCategory: Category? = null

    fun addCategory(category: Category) {
        viewModelScope.launch {
            try {
                val existingCategory = _categories.value.find {
                    it.name.equals(category.name, ignoreCase = true)
                }

                if (category.id == 0) {
                    if (existingCategory != null) {
                        _uiMessage.emit("Category with this name already exists")
                        return@launch
                    }
                    repository.addCategory(category)
                    _uiMessage.emit("Category added successfully")
                } else {
                    repository.updateCategory(category)
                    _uiMessage.emit("Category updated successfully")
                }
            } catch (e: Exception) {
                _uiMessage.emit("Failed to add category")
            }
        }
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        try {
            repository.deleteCategory(category)
            _uiMessage.emit("Category deleted successfully")
        } catch (e: Exception) {
            _uiMessage.emit("Failed to delete category")
        }
    }

    fun clearUiMessage() = viewModelScope.launch {
        _uiMessage.emit(null)
    }

    fun getCategories() = viewModelScope.launch {
        _isLoading.value = true
        repository.getAllCategories()
            .collect { categories ->
                _categories.value = categories
                _isLoading.value = false
            }
    }

}