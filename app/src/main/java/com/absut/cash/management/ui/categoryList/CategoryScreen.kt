package com.absut.cash.management.ui.categoryList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.TempleHindu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.ui.component.IconPickerDropdownMenu
import com.absut.cash.management.ui.component.StoredIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: CategoryListViewModel,
    navController: NavController
) {
    val uiMessage by viewModel.uiMessage.collectAsState(initial = null)
    val categories by viewModel.categories.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteAlertDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUiMessage()
        }
    }

    LaunchedEffect(true) {
        viewModel.getCategories()
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                viewModel.selectedCategory = null
            },
            sheetState = rememberModalBottomSheetState()
        ) {
            AddCategoryDialog(
                onAddCategory = { id, iconId, title ->
                    if (id == 0) { //new category
                        viewModel.addCategory(Category(iconId = iconId, name = title.trim()))
                    } else { //update category
                        viewModel.addCategory(Category(id = id!!, iconId = iconId, name = title.trim()))
                    }
                    showBottomSheet = false
                    viewModel.selectedCategory = null
                },
                category = viewModel.selectedCategory,
            )
        }
    }

    if (showDeleteAlertDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteAlertDialog = false
                viewModel.selectedCategory = null
            },
            title = {
                Text(text = "Delete Category")
            },
            text = {
                Text(text = "Are you sure you want to delete this category?")
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = {
                        viewModel.selectedCategory?.let { category ->
                            viewModel.deleteCategory(category)
                        }
                        showDeleteAlertDialog = false
                        viewModel.selectedCategory = null
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
                        viewModel.selectedCategory = null
                    },
                    content = { Text("Cancel") }
                )
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categories") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showBottomSheet = true },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add category",
                    modifier = Modifier.size(44.dp))
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        when {
            isLoading -> {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator()
                }
            }

            categories.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(contentPadding)
                ) {
                    items(categories) { category ->
                        CategoryListItem(
                            category,
                            onUpdate = {
                                viewModel.selectedCategory = category
                                showBottomSheet = true
                            },
                            onDelete = {
                                viewModel.selectedCategory = category
                                showDeleteAlertDialog = true
                            }
                        )
                    }
                }
            }

            else -> { //empty screen
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(imageVector = Icons.Outlined.TempleHindu, contentDescription = null)
                    Text(text = "No categories")
                }
            }
        }

    }
}

@Composable
fun CategoryListItem(
    category: Category,
    modifier: Modifier = Modifier,
    onUpdate: (Category) -> Unit,
    onDelete: (Category) -> Unit
) {

    var showMenu by remember { mutableStateOf(false) }

    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(category.name)
        },
        leadingContent = {
            Icon(
                StoredIcon.asImageVector(category.iconId ?: 0),
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
    onAddCategory: (Int?, Int?, String) -> Unit,
    modifier: Modifier = Modifier,
    category: Category? = null
) {
    var categoryTitle by remember { mutableStateOf(category?.name ?: "") }
    var iconId by remember { mutableStateOf(category?.iconId ?: 0) }
    var isError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    /* BasicAlertDialog(
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

     }*/

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
                .fillMaxWidth()
                .focusRequester(focusRequester),
            supportingText = if (isError) {
                { Text("Title cannot be empty") }
            } else null,
            leadingIcon = {
                IconPickerDropdownMenu(
                    currentIcon = StoredIcon.asImageVector(iconId),
                    onSelectedIconClick = { iconId = it },
                    onClick = { focusManager.clearFocus() },
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (categoryTitle.isNotBlank()) {
                    onAddCategory(category?.id ?: 0, iconId, categoryTitle)
                    categoryTitle = ""
                    iconId = 0
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

        /*LaunchedEffect(categoryTitle) {
            if (categoryTitle.isBlank()) {
                focusRequester.requestFocus()
            }
        }*/
    }
}


@Preview
@Composable
private fun MainScreenPreview() {
    CategoryScreen(
        viewModel = viewModel<CategoryListViewModel>(),
        navController = rememberNavController()
    )
}

@Preview
@Composable
private fun ListItemPreview() {
    val category = Category(0, "Food")
    Surface {
        CategoryListItem(
            category = category, modifier = Modifier.padding(16.dp),
            onUpdate = {}, onDelete = {})
    }
}

@Preview
@Composable
private fun AddCategoryPreview() {
    Surface {
        AddCategoryDialog(onAddCategory = { a, b, c ->

        })
    }
}