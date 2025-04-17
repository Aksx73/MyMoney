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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.absut.cash.management.data.model.Entry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryListScreen(modifier: Modifier = Modifier /*viewModel: EntryListViewModel*/) {

    //val entries by viewModel.entriesList
    var showMenu by remember { mutableStateOf(false) }

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
                            text = { Text("Delete All Entries") },
                            onClick = { showMenu = false },
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
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            SummaryCard()

            Text(
                text = "Entries",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            EntryListContent(
                entries = emptyList(),
                modifier = Modifier.weight(1f)
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Button(
                    onClick = {},
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
                    onClick = {},
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
fun SummaryCard(modifier: Modifier = Modifier) {
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
                    text = "₹10820",
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
                    text = "₹17000",
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
                    text = "₹6100",
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
fun EntryListContent(modifier: Modifier = Modifier, entries: List<Entry>) {
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
    entry: Entry,
    modifier: Modifier = Modifier,
    onUpdate: (Entry) -> Unit = {},
    onDelete: (Entry) -> Unit = {}
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
                text = "${entry.category}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.constrainAs(category) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            Text(
                text = " • ${entry.updatedDate}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .constrainAs(date) {
                        top.linkTo(parent.top)
                        start.linkTo(category.end)
                    }
                //.padding(start = 8.dp)
            )

            Text(
                text = entry.description ?: "--",
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
                text = "₹${entry.entry_amount}",
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
                        text = { Text("Update") },
                        onClick = {
                            onUpdate(entry)
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
    EntryListScreen()
}

@Preview
@Composable
private fun EntryListContentPreview() {
    val list = listOf(
        Entry(
            _id = 1,
            updated_at = System.currentTimeMillis(),
            description = "Sample description",
            entry_amount = 3423,
            entry_type = 1,
            book_id = 1,
            category_id = 1,
            category = "travel"
        ),
        Entry(
            _id = 1,
            updated_at = System.currentTimeMillis(),
            description = "Sample description",
            entry_amount = 3423,
            entry_type = 1,
            book_id = 1,
            category_id = 1,
            category = "travel"
        ),
        Entry(
            _id = 1,
            updated_at = System.currentTimeMillis(),
            description = "Sample description",
            entry_amount = 3423,
            entry_type = 1,
            book_id = 1,
            category_id = 1,
            category = "travel"
        )
    )
    Surface(Modifier.fillMaxSize()) {
        EntryListContent(entries = list)
    }
}

@Preview
@Composable
fun SummaryCardPreview(modifier: Modifier = Modifier) {
    Surface {
        SummaryCard(modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
private fun EntryListItemPreview() {
    val entry = Entry(
        _id = 1,
        updated_at = System.currentTimeMillis(),
        description = "Sample description",
        entry_amount = 3423,
        entry_type = 1,
        book_id = 1,
        category_id = 1,
        category = "travel"
    )
    Surface {
        EntryListItem(entry = entry, modifier = Modifier.padding(16.dp))
    }
}