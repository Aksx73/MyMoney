package com.absut.cash.management.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.absut.cash.management.ui.booklist.BookListScreen
import com.absut.cash.management.ui.booklist.BookListViewModel
import com.absut.cash.management.ui.categoryList.CategoryListViewModel
import com.absut.cash.management.ui.categoryList.CategoryScreen
import com.absut.cash.management.ui.entrylist.AddUpdateEntryScreen
import com.absut.cash.management.ui.entrylist.EntryType
import com.absut.cash.management.ui.entrylist.EntryListScreen
import com.absut.cash.management.ui.entrylist.EntryListViewModel
import kotlinx.serialization.Serializable


@Serializable
data object BookListRoute

@Serializable
data class EntryListRoute(
    val bookId: Int,
)

@Serializable
data class AddUpdateEntryRoute(
    val entryType: EntryType,
    val bookId: Int,
    val entryId: Int? = null,
)

@Serializable
data object CategoryListRoute


@Composable
fun AppNavigation(navController: NavHostController) {

    val sharedViewModel: EntryListViewModel = hiltViewModel<EntryListViewModel>()

    NavHost(
        navController = navController,
        startDestination = BookListRoute,
    ) {
        composable<BookListRoute> {
            BookListScreen(
                viewModel = hiltViewModel<BookListViewModel>(),
                navController = navController
            )
        }
        composable<EntryListRoute> {
            val args = it.toRoute<EntryListRoute>()
            EntryListScreen(
                viewModel = sharedViewModel,
                navController = navController,
                bookId = args.bookId
            )
        }
        composable<AddUpdateEntryRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<AddUpdateEntryRoute>()
            AddUpdateEntryScreen(
                viewModel = sharedViewModel,
                navController = navController,
                entryType = args.entryType,
                bookId = args.bookId,
                entryId = args.entryId,
            )
        }
        composable<CategoryListRoute> {
            CategoryScreen(
                viewModel = hiltViewModel<CategoryListViewModel>(),
                navController = navController
            )
        }
    }
}