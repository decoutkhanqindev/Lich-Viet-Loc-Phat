package com.decoutkhanqindev.lich_viet_loc_phat.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun NoInternetDialog(onOpenSettings: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* Do nothing, force user to interact */ },
        title = { Text("No Internet Connection") },
        text = { Text("Please check your internet connection and try again.") },
        confirmButton = {
            TextButton(onClick = onOpenSettings) {
                Text("Open Settings")
            }
        }
    )
}