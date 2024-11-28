package com.kafka.kms.ui.upload.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UploadBottomBar(
    isEnabled: Boolean,
    onUploadClick: () -> Unit,
    uploadStatus: String,
    errorMessage: String,
    isEditMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                if (uploadStatus.isNotEmpty()) {
                    Text(uploadStatus, color = MaterialTheme.colorScheme.primary)
                }
                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
            
            Button(
                onClick = onUploadClick,
                enabled = isEnabled
            ) {
                Text(if (isEditMode) "Update" else "Upload")
            }
        }
    }
} 