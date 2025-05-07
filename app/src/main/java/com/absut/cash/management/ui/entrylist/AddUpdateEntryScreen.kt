package com.absut.cash.management.ui.entrylist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.absut.cash.management.R
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.ui.component.SnackbarHostWithController
import com.absut.cash.management.ui.component.StoredIcon
import com.absut.cash.management.util.getFormattedDate

enum class EntryType(val value: Int) {
    CASH_IN(0),
    CASH_OUT(1)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUpdateEntryScreen(
    modifier: Modifier = Modifier,
    viewModel: EntryListViewModel,
    navController: NavController,
    entryType: EntryType,
    bookId: Int,
    entryId: Int? = null
) {
    var currentEntryType by remember { mutableStateOf(entryType) }
    val transactionTypeOptions = listOf("Cash In", "Cash Out")
    var amountText by remember {
        mutableStateOf(
            if (entryId != null) {
                viewModel.selectedEntry?.entry?.entryAmount.toString()
            } else {
                ""
            }
        )
    }
    var dateText by remember {
        mutableStateOf(
            if (entryId != null) {
                viewModel.selectedEntry?.entry?.updatedAt?.getFormattedDate()
                    ?: System.currentTimeMillis().getFormattedDate()
            } else {
                System.currentTimeMillis().getFormattedDate()
            }
        )
    }
    var remarkText by remember {
        mutableStateOf(
            if (entryId != null) {
                viewModel.selectedEntry?.entry?.description.toString()
            } else {
                ""
            }
        )
    }
    var categoryDropDownExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember {
        mutableStateOf(
            if (entryId != null) {
                viewModel.selectedEntry?.category ?: viewModel.defaultCategory
            } else {
                viewModel.defaultCategory
            }
        )
    }
    val categories by viewModel.categories.collectAsState(listOf(viewModel.defaultCategory))
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (entryId != null) {
            viewModel.selectedEntry?.entry?.updatedAt ?: System.currentTimeMillis()
        } else {
            System.currentTimeMillis()
        }
    )
    var isError by remember { mutableStateOf(false) }
    val uiMessage by viewModel.uiMessage.collectAsState(initial = null)
    val snackbarHostState = remember { SnackbarHostState() }
    var saveInProgress by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())


    LaunchedEffect(true) {
        viewModel.getCategories()
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        dateText = millis.getFormattedDate()
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUiMessage()
        }
    }

    LaunchedEffect(viewModel.entryAddUpdateSuccess) {
        if (viewModel.entryAddUpdateSuccess) {
            viewModel.resetEntryAddUpdateSuccess()
            navController.navigateUp()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(if (entryId == null) "New Transaction" else "Edit Transaction") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (amountText.isNotBlank() && amountText.toInt() > 0) {
                        saveInProgress = true
                        val entry = Entry(
                            id = entryId ?: 0,
                            entryAmount = amountText.toInt(),
                            entryType = currentEntryType.value,
                            bookId = bookId,
                            categoryId = if (selectedCategory.id > 0) selectedCategory.id else null,
                            updatedAt = datePickerState.selectedDateMillis
                                ?: System.currentTimeMillis(),
                            description = remarkText
                        )
                        if (entryId == null || entryId == 0) {
                            viewModel.addEntry(entry)
                        } else {
                            viewModel.updateEntry(entry)
                        }
                    } else {
                        isError = true
                        saveInProgress = false
                    }
                },
                enabled = !saveInProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                //.imePadding()
            ) {
                Text("Save")
            }
        },
        snackbarHost = { SnackbarHostWithController(snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.size(8.dp))
            SingleChoiceSegmentedButtonRow(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    //.alpha(if (entryId == null) 1f else 0.6f), // Make it look disabled
            ) {
                transactionTypeOptions.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = transactionTypeOptions.size
                        ),
                        onClick = {
                            currentEntryType =
                                if (index == 0) EntryType.CASH_IN else EntryType.CASH_OUT
                            //save in viewmodel if needed
                        },
                        selected = when (currentEntryType) {
                            EntryType.CASH_IN -> index == 0
                            EntryType.CASH_OUT -> index == 1
                        },
                        label = { Text(label) },
                        //enabled = entryId == null,
                        icon = {
                            Icon(
                                painterResource(if (index == 0) R.drawable.ic_trending_up_24 else R.drawable.ic_trending_down_24),
                                contentDescription = label
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.size(16.dp))

            var amountTextFieldFocus by remember { mutableStateOf(false) }
            val amountFocusRequester = remember { FocusRequester() }

            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    //val regex = """^\d*\.?\d{0,2}$""".toRegex() // Allow numbers and one decimal point with up to 2 decimal places
                    val regex = "^\\d+$".toRegex() // Only allow positive integers
                    if (it.isEmpty() || it.matches(regex)) {
                        amountText = it
                        isError = false
                    }
                },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = isError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(amountFocusRequester)
                    .onFocusChanged { focusState ->
                        amountTextFieldFocus = focusState.isFocused
                    },
                singleLine = true,
                supportingText = if (isError) {
                    { Text("Enter a valid amount") }
                } else null,
                trailingIcon = {
                    if (amountText.isNotEmpty() && amountTextFieldFocus) {
                        IconButton(onClick = { amountText = "" }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_cancel_24),
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
            )

            Spacer(Modifier.size(16.dp))

            ExposedDropdownMenuBox(
                expanded = categoryDropDownExpanded,
                onExpandedChange = { categoryDropDownExpanded = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = selectedCategory.name,
                    onValueChange = {},
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            StoredIcon.asImageVector(selectedCategory.iconId ?: 0),
                            contentDescription = "Category icon",
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropDownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    label = { Text("Category") }
                )
                ExposedDropdownMenu(
                    expanded = categoryDropDownExpanded,
                    onDismissRequest = { categoryDropDownExpanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            leadingIcon = {
                                Icon(
                                    StoredIcon.asImageVector(category.iconId ?: 0),
                                    contentDescription = "Category icon",
                                )
                            },
                            onClick = {
                                selectedCategory = category
                                categoryDropDownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.size(16.dp))

            OutlinedTextField(
                value = dateText,
                onValueChange = {
                    // Do nothing. Date is changed by the date picker.
                },
                label = { Text("Date") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = "Select date",
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        showDatePicker = true
                    },
                singleLine = true,
            )

            Spacer(Modifier.size(16.dp))

            var remarkTextFieldFocus by remember { mutableStateOf(false) }
            val remarkFocusRequester = remember { FocusRequester() }

            OutlinedTextField(
                value = remarkText,
                onValueChange = {
                    remarkText = it
                },
                label = { Text("Remark") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(remarkFocusRequester)
                    .onFocusChanged { focusState ->
                        remarkTextFieldFocus = focusState.isFocused
                    },
                trailingIcon = {
                    if (remarkText.isNotEmpty() && remarkTextFieldFocus) {
                        IconButton(onClick = { remarkText = "" }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_cancel_24),
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
            )

            Spacer(Modifier.size(16.dp))
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AddUpdateEntryScreen(
        viewModel = viewModel<EntryListViewModel>(),
        navController = rememberNavController(),
        entryType = EntryType.CASH_OUT,
        bookId = 1, entryId = null
    )
}

