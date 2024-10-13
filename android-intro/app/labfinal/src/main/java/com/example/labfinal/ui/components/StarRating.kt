package com.example.labfinal.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dung Pham 2217963 12.10.24
// Composable that provides star rating component to be used in the grading of MP, consulted from the
// internet and chatGpt
@Composable
fun StarRating(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        for (i in 1..5) {
            IconButton(
                onClick = { onRatingChanged(i) }
            ) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "$i star",
                    tint = if (i <= rating) Color.Yellow else Color.Gray
                )
            }
        }
    }
}