package com.absut.cash.management.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TextWithBackground(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    enabled : Boolean = true
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (enabled) backgroundColor else Color.Gray.copy(alpha = 0.3f),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = if (enabled) Color.Unspecified else Color.Gray
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TextWithBackground(
        text = "Uncategorized",
        modifier = Modifier.padding(24.dp)
    )
}

@Preview
@Composable
private fun Preview2() {
    TextWithBackground(
        text = "Uncategorized",
        modifier = Modifier.padding(24.dp),
        enabled = false
    )
}