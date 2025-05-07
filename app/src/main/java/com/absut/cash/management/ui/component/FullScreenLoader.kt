package com.absut.cash.management.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp

@Composable
fun FullScreenLoader(modifier: Modifier = Modifier, loaderSize: Dp? = null) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        loaderSize?.let {
            CircularProgressIndicator(Modifier.size(it))
        } ?: run {
            CircularProgressIndicator()
        }
    }

}