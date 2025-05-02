package com.absut.cash.management.ui.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.ui.CategoryListRoute
import com.absut.cash.management.ui.EntryListRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    modifier: Modifier = Modifier,
    viewModel: BookListViewModel,
    navController: NavController
) {
    var showAddBookBottomSheet by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val books by viewModel.books.collectAsState(initial = emptyList())
    val uiMessage by viewModel.uiMessage.collectAsState(initial = null)
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteAlertDialog by remember { mutableStateOf(false) }


    //to show bottom sheet
    if (showAddBookBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.selectedBook = null
                showAddBookBottomSheet = false
            },
            sheetState = rememberModalBottomSheetState()
        ) {
            AddBookBottomSheet(
                onAddBook = { id, title ->
                    if (id == 0) { // new book
                        viewModel.addBook(Book(title = title.trim()))
                    } else { // update book
                        viewModel.updateBookTitle(bookId = id, title = title.trim())
                    }
                    showAddBookBottomSheet = false
                    viewModel.selectedBook = null
                },
                book = viewModel.selectedBook,
            )
        }
    }

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUiMessage()
        }
    }

    LaunchedEffect(true) {
        viewModel.getAllBooks()
    }

    if (showDeleteAlertDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteAlertDialog = false
                viewModel.selectedBook = null
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = "Delete All",
                )
            },
            title = {
                Text(text = "Delete ${viewModel.selectedBook?.title}?")
            },
            text = {
                Text(text = "This will permanently delete the book and all its entries.")
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = {
                        viewModel.selectedBook?.let { book ->
                            viewModel.deleteBook(book)
                        }
                        showDeleteAlertDialog = false
                        viewModel.selectedBook = null
                    },
                    content = { Text("Delete") }
                )
            },
            dismissButton = {
                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {
                        showDeleteAlertDialog = false
                        viewModel.selectedBook = null
                    },
                    content = { Text("Cancel") }
                )
            }
        )
    }

    //main content
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Books") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Categories") },
                            onClick = {
                                navController.navigate(CategoryListRoute)
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Category,
                                    contentDescription = "Categories"
                                )
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showAddBookBottomSheet = true },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Book,
                    contentDescription = "Add Book",
                    modifier = Modifier.size(44.dp)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 112.dp
                )
            ) {
                items(books) { book ->
                    BookListItem(
                        book = book,
                        onBookClick = { navController.navigate(EntryListRoute(bookId = book.id)) },
                        onEdit = {
                            viewModel.selectedBook = book
                            showAddBookBottomSheet = true
                        },
                        onDelete = {
                            viewModel.selectedBook = book
                            showDeleteAlertDialog = true
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun BookListContent(
    modifier: Modifier = Modifier,
    books: List<Book>,
    navController: NavController
) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(books) { book ->
                BookListItem(
                    book = book,
                    onBookClick = { navController.navigate(EntryListRoute(bookId = book.id)) },
                    onEdit = { },
                    onDelete = {}
                )
            }
        }
    }
}

@Composable
fun BookListItem(
    book: Book,
    modifier: Modifier = Modifier,
    onBookClick: (Book) -> Unit = {},
    onEdit: (Book) -> Unit = {},
    onDelete: (Book) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    OutlinedCard(
        onClick = { onBookClick(book) },
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = book.title,
                    Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = "₹${book.bookAmount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (book.bookAmount >= 0) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    maxLines = 1,
                )
            }
            IconButton(
                onClick = {
                    showMenu = true
                },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                )
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Rename") },
                        onClick = {
                            onEdit(book)
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Rename"
                            )
                        }
                    )
                    DropdownMenuItem(

                        text = { Text("Delete") },
                        onClick = {
                            onDelete(book)
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete Book"
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.size(8.dp))
        }
    }
}

@Composable
fun AddBookBottomSheet(
    onAddBook: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
    book: Book? = null
) {
    var bookTitle by remember { mutableStateOf(book?.title ?: "") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add New Book",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = bookTitle,
            onValueChange = {
                bookTitle = it
                isError = false
            },
            label = { Text("Book Title") },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth(),
            supportingText = if (isError) {
                { Text("Title cannot be empty") }
            } else null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (bookTitle.isNotBlank()) {
                    onAddBook(book?.id ?: 0, bookTitle)
                    bookTitle = ""
                } else {
                    isError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = bookTitle.isNotBlank()
        ) {
            Text("Add Book")
        }
    }
}


/*@Preview(showBackground = true)
@Composable
fun BookListScreenPreview() {
    BookListScreen(viewModel = viewModel<BookListViewModel>())
}*/

@Preview(showBackground = true)
@Composable
fun BookListContentPreview() {
    val sampleBooks = listOf(
        Book(1, "January 2025", 215000, 1, 1),
        Book(2, "Dubai Trip", 2000, 1, 1),
        Book(3, "Sample Book Title", 3000, 1, 1)
    )
    BookListContent(books = sampleBooks, navController = rememberNavController())
}

@Preview
@Composable
private fun AddBookPreview() {
    Surface {
        AddBookBottomSheet(onAddBook = { sd, s ->

        })
    }
}

@Preview
@Composable
private fun BookListItemPreview() {
    val book = Book(1, "Book 1", 1000, 1, 1)
    Surface {
        BookListItem(book = book, modifier = Modifier.padding(16.dp))
    }

}