package com.absut.cash.management.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.absut.cash.management.ui.booklist.BookListScreen
import com.absut.cash.management.ui.booklist.BookListViewModel
import com.absut.cash.management.ui.categoryList.CategoryListViewModel
import com.absut.cash.management.ui.categoryList.CategoryScreen
import com.absut.cash.management.ui.entrydetail.AddUpdateEntryScreen
import com.absut.cash.management.ui.entrydetail.EntryDetailViewModel
import com.absut.cash.management.ui.entrydetail.EntryType
import com.absut.cash.management.ui.entrylist.EntryListScreen
import com.absut.cash.management.ui.entrylist.EntryListViewModel
import kotlinx.serialization.Serializable


/*object NavigationRoutes {
    const val BOOK_LIST = "bookList"
    const val ENTRY_LIST = "entryList"
    const val ADD_UPDATE_ENTRY = "addUpdateEntry"
    const val CATEGORY_LIST = "categoryList"
}*/

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
    val entryId: Int? = null
)

@Serializable
data object CategoryListRoute


@Composable
fun AppNavigation(navController: NavHostController) {
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
                viewModel = hiltViewModel<EntryListViewModel>(),
                navController = navController,
                bookId = args.bookId
            )
        }
        composable<AddUpdateEntryRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<AddUpdateEntryRoute>()
            AddUpdateEntryScreen(
                viewModel = hiltViewModel<EntryDetailViewModel>(),
                navController = navController,
                entryType = args.entryType,
                bookId = args.bookId,
                entryId = null
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