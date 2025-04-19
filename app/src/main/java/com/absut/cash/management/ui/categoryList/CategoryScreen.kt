package com.absut.cash.management.ui.categoryList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.absut.cash.management.data.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: CategoryListViewModel
) {

    var showBottomSheet by remember { mutableStateOf(false) }


    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            AddCategoryDialog(
                onAddCategory = { title ->
                    //todo handle adding category here
                    showBottomSheet = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categories") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true }
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
            }
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            items(3) {
                CategoryListItem(category = Category(it, "Category $it", it * 10))
            }
        }


    }
}

@Composable
fun CategoryListItem(
    category: Category,
    modifier: Modifier = Modifier,
    onUpdate: (Category) -> Unit = {},
    onDelete: (Category) -> Unit = {}
) {

    var showMenu by remember { mutableStateOf(false) }

    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(category.name)
        },
        leadingContent = {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = "Category icon",
            )
        },
        trailingContent = {
            IconButton(onClick = {
                showMenu = true
            }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Delete"
                )

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Update") },
                        onClick = {
                            onUpdate(category)
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
                            onDelete(category)
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryDialog(
    onAddCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var categoryTitle by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = {}
    ) {
        //todo
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text =
                        "This area typically contains the supportive text " +
                                "which presents the details regarding the Dialog's purpose.",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = { onAddCategory(categoryTitle) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Confirm")
                }
            }
        }

    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "New Category",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = categoryTitle,
            onValueChange = {
                categoryTitle = it
                isError = false
            },
            label = { Text("Category Title") },
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
                if (categoryTitle.isNotBlank()) {
                    onAddCategory(categoryTitle)
                    categoryTitle = ""
                } else {
                    isError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = categoryTitle.isNotBlank()
        ) {
            Text("Add Category")
        }
    }
}


@Preview
@Composable
private fun MainScreenPreview() {
    CategoryScreen(viewModel = viewModel<CategoryListViewModel>())
}

@Preview
@Composable
private fun ListItemPreview() {
    val category = Category(0, "Food", 23)
    Surface {
        CategoryListItem(category = category, modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
private fun AddCategoryPreview() {
    Surface {
        AddCategoryDialog(onAddCategory = {

        })
    }
}