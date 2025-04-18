package com.absut.cash.management.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.absut.cash.management.ui.booklist.BookListScreen
import com.absut.cash.management.ui.booklist.BookListViewModel
import com.absut.cash.management.ui.categoryList.CategoryListViewModel
import com.absut.cash.management.ui.categoryList.CategoryScreen
import com.absut.cash.management.ui.entrydetail.AddUpdateEntryScreen
import com.absut.cash.management.ui.entrydetail.EntryDetailViewModel
import com.absut.cash.management.ui.entrylist.EntryListScreen
import com.absut.cash.management.ui.entrylist.EntryListViewModel


object NavigationRoutes {
    const val BOOK_LIST = "bookList"
    const val ENTRY_LIST = "entryList"
    const val ADD_UPDATE_ENTRY = "addUpdateEntry"
    const val CATEGORY_LIST = "categoryList"
}

@Composable
fun AppNavigation(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.BOOK_LIST,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(NavigationRoutes.BOOK_LIST) {
            BookListScreen(
                viewModel = hiltViewModel<BookListViewModel>(),
                onNavigateToEntryList = { navController.navigate(NavigationRoutes.ENTRY_LIST) },
                onNavigateToCategoryList = { navController.navigate(NavigationRoutes.CATEGORY_LIST) }
            )
        }
        composable(NavigationRoutes.ENTRY_LIST) {
            EntryListScreen(
                viewModel = hiltViewModel<EntryListViewModel>(),
                onNavigateToAddEntry = { navController.navigate(NavigationRoutes.ADD_UPDATE_ENTRY) }
            )
        }
        composable(NavigationRoutes.ADD_UPDATE_ENTRY) {
            AddUpdateEntryScreen(viewModel = hiltViewModel<EntryDetailViewModel>())
        }
        composable(NavigationRoutes.CATEGORY_LIST) {
            CategoryScreen(viewModel = hiltViewModel<CategoryListViewModel>())
        }
    }
}