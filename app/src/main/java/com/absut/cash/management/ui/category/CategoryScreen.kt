package com.absut.cash.management.ui.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.TempleHindu
import androidx.compose.material.icons.outlined.ToggleOff
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.ui.component.FullScreenLoader
import com.absut.cash.management.ui.component.IconPickerDropdownMenu
import com.absut.cash.management.ui.component.SnackbarHostWithController
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
    var showMenu by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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
            AddCategoryBottomSheet(
                onAddCategory = { id, iconId, title ->
                    if (id == 0) { //new category
                        viewModel.addCategory(Category(iconId = iconId, name = title.trim()))
                    } else { //update category
                        viewModel.addCategory(
                            Category(
                                id = id!!,
                                iconId = iconId,
                                name = title.trim()
                            )
                        )
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
                Text(
                    text = if (viewModel.selectedCategory?.isActive == true) {
                        "Make category inactive?"
                    } else {
                        "Make category active?"
                    }
                )
            },
            text = {
                if (viewModel.selectedCategory?.isActive == true) {
                    Text(text = "This action will make category as inactive and cannot be used for new transactions to maintain data integrity.")
                } else null
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = {
                        viewModel.selectedCategory?.let { category ->
                            viewModel.updateCategoryStatus(category, !category.isActive)
                        }
                        showDeleteAlertDialog = false
                        viewModel.selectedCategory = null
                    },
                    content = { Text("Confirm") }
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
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                            text = {
                                Text(
                                    if (viewModel.showInactiveCategories.collectAsState().value) {
                                        "Hide inactive categories"
                                    } else {
                                        "Show inactive categories"
                                    }
                                )
                            },
                            onClick = {
                                showMenu = false
                                viewModel.toggleInactiveCategories()
                            },
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showBottomSheet = true },
            ) {
                Icon(
                    Icons.Filled.Add, contentDescription = "Add category",
                    modifier = Modifier.size(44.dp)
                )
            }
        },
        snackbarHost = { SnackbarHostWithController(snackbarHostState) }
    ) { contentPadding ->
        when {
            isLoading -> { FullScreenLoader() }

            categories.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(contentPadding),
                    contentPadding = PaddingValues(bottom = 112.dp) // Add bottom padding for FAB
                ) {
                    items(categories, key = {it.id}) { category ->
                        CategoryListItem(
                            category,
                            onUpdate = {
                                viewModel.selectedCategory = category
                                showBottomSheet = true
                            },
                            onDelete = {
                                viewModel.selectedCategory = category
                                showDeleteAlertDialog = true
                            },
                            modifier = Modifier.animateItem(),
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
                    Image(
                        imageVector = Icons.Outlined.TempleHindu,
                        contentDescription = null,
                        modifier = Modifier.size(84.dp),
                        alpha = 0.5f,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                    Spacer(Modifier.size(8.dp))
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
        modifier = modifier.alpha(if (category.isActive) 1f else 0.5f),
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
                    contentDescription = "Options"
                )

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
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
                        text = {
                            Text(
                                if (category.isActive) {
                                    "Mark Inactive"
                                } else {
                                    "Mark Active"
                                }
                            )
                        },
                        onClick = {
                            onDelete(category)
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (category.isActive) {
                                    Icons.Outlined.ToggleOn
                                } else {
                                    Icons.Outlined.ToggleOff
                                },
                                contentDescription = "Make Inactive"
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
fun AddCategoryBottomSheet(
    onAddCategory: (Int?, Int?, String) -> Unit,
    modifier: Modifier = Modifier,
    category: Category? = null
) {
    var categoryTitle by remember { mutableStateOf(category?.name ?: "") }
    var iconId by remember { mutableStateOf(category?.iconId ?: 0) }
    var isError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (category == null)"New Category" else "Edit Category",
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
private fun ListItemPreview2() {
    val category = Category(0, "Food", isActive = false)
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
        AddCategoryBottomSheet(onAddCategory = { a, b, c ->

        })
    }
}