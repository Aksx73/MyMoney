package com.absut.cash.management.ui.booklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.absut.cash.management.data.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    modifier: Modifier = Modifier,
    viewModel: BookListViewModel
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val books by viewModel.getAllBooks().collectAsState(initial = emptyList())

    //to show bottom sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            AddBookBottomSheet(
                onAddBook = { title ->
                    //todo handle adding book here
                    showBottomSheet = false
                }
            )
        }
    }

    //main content
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Books") },
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showBottomSheet = true },
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Book",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    ) { contentPadding ->
        /*Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(books) { book ->
                    BookListItem(book = book, onEdit = {}, onDelete = {})
                }
            }

        }*/

        BookListContent(
            modifier = Modifier.padding(contentPadding),
            books = books
        )
    }

}

@Composable
fun BookListContent(modifier: Modifier = Modifier, books: List<Book>) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(books) { book ->
                BookListItem(book = book, onEdit = {}, onDelete = {})
            }
        }
    }
}

@Composable
fun BookListItem(
    book: Book,
    modifier: Modifier = Modifier,
    onEdit: (Book) -> Unit = {},
    onDelete: (Book) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = {})
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
                    text = "₹${book.book_amount}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                )
            }
            Box (
                modifier = Modifier
                    .size(48.dp) // Ensure a minimum size of 48dp
                    .clickable(
                        onClick = { showMenu = true },
                        role = Role.Button // for accessibility
                    ),
                contentAlignment = Alignment.Center,
            ){
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
    onAddBook: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var bookTitle by remember { mutableStateOf("") }
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
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (bookTitle.isNotBlank()) {
                    onAddBook(bookTitle)
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
        Book(1, "The Hitchhiker's Guide to the Galaxy", 1000, 1, 1),
        Book(2, "Pride and Prejudice", 2000, 1, 1),
        Book(3, "To Kill a Mockingbird", 3000, 1, 1)
    )
    BookListContent(books = sampleBooks)
}

@Preview
@Composable
private fun AddBookPreview() {
    Surface {
        AddBookBottomSheet(onAddBook = {

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