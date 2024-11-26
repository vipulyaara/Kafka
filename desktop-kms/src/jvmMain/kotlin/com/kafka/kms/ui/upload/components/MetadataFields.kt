package com.kafka.kms.ui.upload.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.kms.components.TextField

@Composable
fun MetadataFields(
    subjects: String,
    onSubjectsChange: (String) -> Unit,
    collections: String,
    onCollectionsChange: (String) -> Unit,
    languages: String,
    onLanguagesChange: (String) -> Unit,
    sourceUrls: String,
    onSourceUrlsChange: (String) -> Unit,
    copyright: String,
    onCopyrightChange: (String) -> Unit,
    dateFeatured: String,
    onDateFeaturedChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = subjects,
                onValueChange = onSubjectsChange,
                placeholder = "Subjects",
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = collections,
                onValueChange = onCollectionsChange,
                placeholder = "Collections",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = languages,
                onValueChange = onLanguagesChange,
                placeholder = "Languages",
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = sourceUrls,
                onValueChange = onSourceUrlsChange,
                placeholder = "Source URLs",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = copyright,
                onValueChange = onCopyrightChange,
                placeholder = "Copyright",
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = dateFeatured,
                onValueChange = { 
                    if (it.isEmpty() || it.matches(Regex("""^\d{4}-\d{2}-\d{2}$"""))) {
                        onDateFeaturedChange(it)
                    }
                },
                placeholder = "Date Featured (YYYY-MM-DD)",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
} 