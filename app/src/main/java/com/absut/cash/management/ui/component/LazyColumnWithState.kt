package com.absut.cash.management.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LazyColumnWithState(
    isLoading: Boolean,
    isError: Boolean,
    items: List<Any>,
    emptyContent: @Composable () -> Unit = {},
    itemContent: @Composable (Any) -> Unit
) {
    when {
        isLoading -> FullScreenLoader()
        isError -> Text("Something went wrong!")
        items.isEmpty() -> emptyContent()
        else -> LazyColumn {
            items(items) { item ->
                itemContent(item)
            }
        }
    }
}