package com.absut.cash.management.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch(Dispatchers.IO) {
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

    fun deleteCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.deleteCategory(category)
            _uiMessage.emit("Category deleted successfully")
        } catch (e: Exception) {
            _uiMessage.emit("Failed to delete category")
        }
    }

    fun updateCategoryStatus(category: Category, isActive: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.updateCategory(category.copy(isActive = isActive))
            _uiMessage.emit("Category marked as ${if (isActive) "active" else "inactive"}")
        } catch (e: Exception) {
            _uiMessage.emit("Failed to mark category as ${if (isActive) "active" else "inactive"}")
        }
    }

    fun clearUiMessage() = viewModelScope.launch {
        _uiMessage.emit(null)
    }

    fun getCategories() = viewModelScope.launch(Dispatchers.IO) {
        _isLoading.value = true
        val categoriesFlow = if (_showInactiveCategories.value) {
            repository.getAllCategories()
        } else {
            repository.getActiveCategories()
        }
        categoriesFlow.collect { categories ->
            _categories.value = categories
            _isLoading.value = false
        }
    }

    private val _showInactiveCategories = MutableStateFlow(false)
    val showInactiveCategories = _showInactiveCategories.asStateFlow()
    fun toggleInactiveCategories() {
        _showInactiveCategories.value = !_showInactiveCategories.value
        getCategories() // Refresh list
    }
}