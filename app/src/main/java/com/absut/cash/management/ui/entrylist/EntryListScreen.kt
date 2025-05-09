package com.absut.cash.management.ui.entrylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ShapeDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.model.EntryWithCategory
import com.absut.cash.management.ui.AddUpdateEntryRoute
import com.absut.cash.management.ui.component.FullScreenLoader
import com.absut.cash.management.ui.component.SnackbarHostWithController
import com.absut.cash.management.ui.component.TextWithBackground
import com.absut.cash.management.util.toEntryType

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
    var showDeleteAlertDialog by remember { mutableStateOf(false) }
    var showDeleteAllAlertDialog by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading

    LaunchedEffect(bookId) {
        viewModel.getBookFromId(bookId)
        viewModel.getEntriesOfBook(bookId)
    }

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUiMessage()
        }
    }

    if (showDeleteAlertDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteAlertDialog = false
                viewModel.selectedEntry = null
            },
            title = {
                Text(text = "Delete Entry")
            },
            text = {
                Text(text = "Are you sure you want to delete this entry of amount ₹${viewModel.selectedEntry?.entry?.entryAmount}?")
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = {
                        viewModel.selectedEntry?.let { entry ->
                            viewModel.deleteEntry(entry.entry)
                        }
                        showDeleteAlertDialog = false
                        viewModel.selectedEntry = null
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
                        viewModel.selectedEntry = null
                    },
                    content = { Text("Cancel") }
                )
            }
        )
    }

    if (showDeleteAllAlertDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteAllAlertDialog = false
            },
            title = {
                Text(text = "Delete All Entries")
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = "Delete All",
                )
            },
            text = {
                Text(text = "Are you sure you want to delete all ${entries.size} entries in this book(${book?.title})?")
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = {
                        viewModel.deleteAllEntries(bookId)
                        showDeleteAllAlertDialog = false
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
                        showDeleteAllAlertDialog = false
                    },
                    content = { Text("Cancel") }
                )
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Book Details", style = MaterialTheme.typography.titleLarge)
                        Text(
                            book?.title ?: "",
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
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
                                showDeleteAllAlertDialog = true
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
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
        },
        snackbarHost = { SnackbarHostWithController(snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            SummaryCard(b = book)

            Text(
                text = "Entries",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            when {
                isLoading -> {
                    FullScreenLoader(
                        modifier = Modifier.padding(contentPadding),
                        loaderSize = 24.dp
                    )
                }

                entries.isNotEmpty() -> {
                    EntryListContent(
                        entries = entries,
                        modifier = Modifier.weight(1f, false),
                        onUpdate = { entry ->
                            viewModel.selectedEntry = entry
                            navController.navigate(
                                AddUpdateEntryRoute(
                                    entryType = entry.entry.entryType.toEntryType(),
                                    bookId = entry.entry.bookId,
                                    entryId = entry.entry.id
                                )
                            )
                        },
                        onDelete = { entry ->
                            viewModel.selectedEntry = entry
                            showDeleteAlertDialog = true
                        }
                    )
                }

                else -> { //empty state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SyncAlt,
                            contentDescription = null,
                            modifier = Modifier.size(84.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(text = "No entries found")
                    }
                }
            }


        }


    }
}

@Composable
fun SummaryCard(modifier: Modifier = Modifier, b: Book?) {
    Card(
        modifier = modifier.padding(16.dp),
        shape = ShapeDefaults.ExtraLarge,
        //border = CardDefaults.outlinedCardBorder(),
        /*border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.3f)
        ),*/
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(Modifier.padding(vertical = 16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "Net Balance:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier
                        .padding(start = 20.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "₹${b?.bookAmount ?: 0}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = if ((b?.bookAmount ?: 0) >= 0) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.size(16.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.3f))

            Spacer(Modifier.size(16.dp))

            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "Net Cash In:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 20.dp),
                )
                Text(
                    text = "₹${b?.cashIn ?: 0}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.size(16.dp))

            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "Net Cash Out:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 20.dp),

                    )
                Text(
                    text = "₹${b?.cashOut ?: 0}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EntryListContent(
    modifier: Modifier = Modifier,
    entries: List<EntryWithCategory>,
    onUpdate: (EntryWithCategory) -> Unit = {},
    onDelete: (EntryWithCategory) -> Unit = {}
) {
    Column(modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = entries, key = { it.entry.id }) { entry ->
                EntryListItem(
                    entry = entry,
                    onUpdate = { onUpdate(entry) },
                    onDelete = { onDelete(entry) },
                    modifier = Modifier.animateItem(),
                )
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

    Card(
        modifier = modifier,
        shape = ShapeDefaults.ExtraLarge
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 0.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextWithBackground(
                        text = entry.category?.name ?: "Uncategorized",
                        enabled = entry.category?.isActive ?: true,
                        backgroundColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = entry.entry.formattedUpdatedAt,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                    )
                }

                if (!entry.entry.description.isNullOrBlank()) {
                    Text(
                        text = entry.entry.description,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "₹${entry.entry.entryAmount}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (entry.entry.entryType == EntryType.CASH_OUT.value) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier
                    //.padding(end = 8.dp)
                )
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.padding(end = 6.dp)
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
                categoryId = 2,
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
                description = "",
                entryAmount = 3423,
                entryType = 0,
                bookId = 1,
                categoryId = 3,
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
                description = "Sample description lorem ipsum ample description ample description ample description ample description",
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