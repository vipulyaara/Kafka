package com.kafka.kms.ui.upload.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.kms.components.TextField

@Composable
fun MainBookInfo(
    title: String,
    onTitleChange: (String) -> Unit,
    creators: String,
    onCreatorsChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    id: String,
    onIdChange: (String) -> Unit,
    onIdEdited: () -> Unit,
    publishers: String,
    onPublishersChange: (String) -> Unit,
    translators: String,
    onTranslatorsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Left column - Primary fields
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = "Title",
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = creators,
                onValueChange = onCreatorsChange,
                placeholder = "Creators",
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = onDescriptionChange,
                placeholder = "Description",
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Right column - Secondary fields
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = id,
                onValueChange = { 
                    onIdChange(it)
                    onIdEdited()
                },
                placeholder = "ID (author_title)",
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = publishers,
                onValueChange = onPublishersChange,
                placeholder = "Publishers",
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = translators,
                onValueChange = onTranslatorsChange,
                placeholder = "Translators",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
} 