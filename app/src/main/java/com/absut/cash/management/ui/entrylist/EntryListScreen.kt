package com.absut.cash.management.ui.entrylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.model.EntryWithCategory
import com.absut.cash.management.ui.AddUpdateEntryRoute
import com.absut.cash.management.ui.entrydetail.EntryType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryListScreen(
    viewModel: EntryListViewModel,
    navController: NavController,
    bookId: Int
) {
    var showMenu by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val uiMessage by viewModel.uiMessage.collectAsState(initial = null)
    val book by viewModel.book.collectAsState()
    val entries by viewModel.entries.collectAsState()

    LaunchedEffect(bookId) {
        viewModel.getBookFromId(bookId)
        viewModel.getEntriesOfBook(bookId)
    }

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(book?.title ?: "Book Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
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
                            text = { Text("Delete All Entries") },
                            onClick = {
                                //todo
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete All"
                                )
                            }
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            SummaryCard(b = book)

            Text(
                text = "Entries",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            EntryListContent(
                entries = entries,
                modifier = Modifier.weight(1f)
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Button(
                    onClick = {
                        navController.navigate(
                            AddUpdateEntryRoute(
                                entryType = EntryType.CASH_IN,
                                bookId = bookId,
                                entryId = null
                            )
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("+ Cash In")
                }
                Spacer(Modifier.size(12.dp))
                Button(
                    onClick = {
                        navController.navigate(
                            AddUpdateEntryRoute(
                                entryType = EntryType.CASH_OUT,
                                bookId = bookId,
                                entryId = null
                            )
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("- Cash Out")
                }
            }

        }


    }
}

@Composable
fun SummaryCard(modifier: Modifier = Modifier, b: Book?) {
    OutlinedCard(modifier = modifier.padding(16.dp)) {
        Column(Modifier.padding(vertical = 16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "Net Balance:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "₹${b?.bookAmount ?: 0}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.size(16.dp))

            HorizontalDivider()

            Spacer(Modifier.size(16.dp))

            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "Net Cash In:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 16.dp),
                )
                Text(
                    text = "₹${b?.cashIn ?: 0}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                )
            }

            Spacer(Modifier.size(16.dp))

            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "Net Cash Out:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 16.dp),

                    )
                Text(
                    text = "₹${b?.cashOut ?: 0}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EntryListContent(modifier: Modifier = Modifier, entries: List<EntryWithCategory>) {
    Column(modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(entries) { entry ->
                EntryListItem(entry = entry, onUpdate = {}, onDelete = {})
            }
        }
    }
}

@Composable
fun EntryListItem(
    entry: EntryWithCategory,
    modifier: Modifier = Modifier,
    onUpdate: (EntryWithCategory) -> Unit = {},
    onDelete: (EntryWithCategory) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    OutlinedCard(modifier = modifier) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 0.dp, 16.dp)
        ) {
            val (category, amount, date, description, optionIcon) = createRefs()

            Text(
                text = "${entry.category?.name}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.constrainAs(category) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            Text(
                text = " • ${entry.entry.formattedUpdatedAt}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .constrainAs(date) {
                        top.linkTo(parent.top)
                        start.linkTo(category.end)
                    }
                //.padding(start = 8.dp)
            )

            Text(
                text = entry.entry.description ?: "--",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .constrainAs(description) {
                        start.linkTo(parent.start)
                        top.linkTo(date.bottom)
                        end.linkTo(amount.start)
                        width = Dimension.fillToConstraints
                    }
                    .padding(end = 16.dp, top = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "₹${entry.entry.entryAmount}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .constrainAs(amount) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(optionIcon.start)
                    }
                    .padding(end = 8.dp)
            )

            IconButton(
                onClick = { showMenu = true },
                modifier = Modifier
                    .constrainAs(optionIcon) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More option"
                )

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            onUpdate(entry)
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit"
                            )
                        }
                    )
                    DropdownMenuItem(

                        text = { Text("Delete") },
                        onClick = {
                            onDelete(entry)
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
        }
    }
}

@Preview
@Composable
private fun EntryListScreenPreview() {
    EntryListScreen(
        viewModel = viewModel<EntryListViewModel>(),
        navController = rememberNavController(),
        bookId = 1
    )
}

@Preview
@Composable
private fun EntryListContentPreview() {
    val list = listOf(
        EntryWithCategory(
            Entry(
                id = 1,
                updatedAt = System.currentTimeMillis(),
                description = "Sample description",
                entryAmount = 3423,
                entryType = 1,
                bookId = 1,
                categoryId = 1,
            ),
            Category(
                id = 1,
                name = "Uncategorized",
                iconId = 1,
            )
        ),
        EntryWithCategory(
            Entry(
                id = 1,
                updatedAt = System.currentTimeMillis(),
                description = "Sample description",
                entryAmount = 3423,
                entryType = 1,
                bookId = 1,
                categoryId = 1,
            ),
            Category(
                id = 1,
                name = "Uncategorized",
                iconId = 1,
            )
        ),
        EntryWithCategory(
            Entry(
                id = 1,
                updatedAt = System.currentTimeMillis(),
                description = "Sample description",
                entryAmount = 3423,
                entryType = 1,
                bookId = 1,
                categoryId = 1,
            ),
            Category(
                id = 1,
                name = "Uncategorized",
                iconId = 1,
            )
        )
    )
    Surface(Modifier.fillMaxSize()) {
        EntryListContent(entries = list)
    }
}

@Preview
@Composable
fun SummaryCardPreview() {
    Surface {
        SummaryCard(
            modifier = Modifier.padding(16.dp),
            b = Book(
                id = 1,
                title = "Sample Book",
                cashOut = 213,
                cashIn = 234,
                bookAmount = 3242
            )
        )
    }
}

@Preview
@Composable
private fun EntryListItemPreview() {
    val entry = Entry(
        id = 1,
        updatedAt = System.currentTimeMillis(),
        description = "Sample description",
        entryAmount = 3423,
        entryType = 1,
        bookId = 1,
        categoryId = 1,
    )
    val category = Category(
        id = 1,
        name = "Uncategorized",
        iconId = 1,
    )
    Surface {
        EntryListItem(
            entry = EntryWithCategory(entry, category),
            modifier = Modifier.padding(16.dp)
        )
    }
}