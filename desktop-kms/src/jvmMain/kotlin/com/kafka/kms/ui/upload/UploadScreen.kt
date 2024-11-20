package com.kafka.kms.ui.upload

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kafka.data.model.MediaType
import com.kafka.kms.components.DropdownField
import com.kafka.kms.components.TextField
import com.kafka.kms.service.UploadService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Cursor

@Composable
fun UploadScreen(uploadService: UploadService, modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var longDescription by remember { mutableStateOf("") }
    var creators by remember { mutableStateOf("") }
    var subjects by remember { mutableStateOf("") }
    var publishers by remember { mutableStateOf("") }
    var languages by remember { mutableStateOf("") }
    var collections by remember { mutableStateOf("") }
    var sourceUrls by remember { mutableStateOf("") }
    var translators by remember { mutableStateOf("") }
    var epubFilePath by remember { mutableStateOf("") }
    var coverFilePath by remember { mutableStateOf("") }
    var copyright by remember { mutableStateOf("") }
    var mediaType by remember { mutableStateOf(MediaType.Text) }
    var uploadStatus by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var hasEditedId by remember { mutableStateOf(false) }
    var contentOpfPath by remember { mutableStateOf("") }
    var dateFeatured by remember { mutableStateOf("") }

    var isMediaTypeExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(title, creators) {
        if (!hasEditedId && title.isNotEmpty() && creators.isNotEmpty()) {
            val firstCreator = creators.split(",")[0].trim()
            id = buildString {
                append(
                    firstCreator.lowercase()
                        .replace(Regex("[^a-z0-9]"), "_")
                        .replace(Regex("_+"), "_")
                        .trim('_')
                )
                append("-")
                append(
                    title.lowercase()
                        .replace(Regex("[^a-z0-9]"), "_")
                        .replace(Regex("_+"), "_")
                        .trim('_')
                )
            }
        }
    }

    LaunchedEffect(contentOpfPath) {
        if (contentOpfPath.isNotEmpty()) {
            try {
                val metadata = ContentOpfParser.parse(contentOpfPath)
                title = metadata.title
                creators = metadata.creators.joinToString(", ")
                description = metadata.description
                longDescription = metadata.longDescription.trim()
                subjects = metadata.subjects
                    .flatMap { subject -> subject.split("--").map { it.trim() } }
                    .distinct()
                    .joinToString(", ")
                publishers = metadata.publishers.joinToString(", ")
                languages = metadata.languages.joinToString(", ")
                copyright = metadata.rights
                translators = metadata.translators.joinToString(", ")
                sourceUrls = metadata.sources.joinToString(", ")
            } catch (e: Exception) {
                errorMessage = "Failed to parse content.opf: ${e.message}"
            }
        }
    }

    Surface {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title section
            Text("Upload New Book", style = MaterialTheme.typography.headlineMedium)

            // Main book information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = "Title",
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = "Description",
                        modifier = Modifier.fillMaxWidth()
                    )

                    LongDescription(longDescription, Modifier.fillMaxWidth()) {
                        longDescription = it
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = id,
                        onValueChange = {
                            id = it
                            hasEditedId = true
                        },
                        placeholder = "ID (author_title)",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextField(
                            value = contentOpfPath,
                            onValueChange = { contentOpfPath = it },
                            placeholder = "content.opf File",
                            modifier = Modifier.weight(1f),
                            readOnly = true
                        )
                        TextButton(onClick = { chooseFile("opf") { contentOpfPath = it } }) {
                            Text("Choose opf")
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextField(
                            value = epubFilePath,
                            onValueChange = { epubFilePath = it },
                            placeholder = "EPUB File",
                            modifier = Modifier.weight(1f),
                            readOnly = true
                        )
                        TextButton(onClick = { chooseFile("epub") { epubFilePath = it } }) {
                            Text("Choose EPUB")
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextField(
                            value = coverFilePath,
                            onValueChange = { coverFilePath = it },
                            placeholder = "Cover Image",
                            modifier = Modifier.weight(1f),
                            readOnly = true
                        )
                        TextButton(onClick = { chooseFile("image") { coverFilePath = it } }) {
                            Text("Choose Cover")
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = creators,
                        onValueChange = { creators = it },
                        placeholder = "Creators",
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = publishers,
                        onValueChange = { publishers = it },
                        placeholder = "Publishers",
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = collections,
                        onValueChange = { collections = it },
                        placeholder = "Collections",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = subjects,
                        onValueChange = { subjects = it },
                        placeholder = "Subjects",
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = languages,
                        onValueChange = { languages = it },
                        placeholder = "Languages",
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = sourceUrls,
                        onValueChange = { sourceUrls = it },
                        placeholder = "Source URLs",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = dateFeatured,
                        onValueChange = { 
                            if (it.isEmpty() || it.matches(Regex("""^\d{4}-\d{2}-\d{2}$"""))) {
                                dateFeatured = it
                            }
                        },
                        placeholder = "Date Featured (YYYY-MM-DD)",
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = translators,
                        onValueChange = { translators = it },
                        placeholder = "Translators",
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = copyright,
                        onValueChange = { copyright = it },
                        placeholder = "Copyright",
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownField(
                        value = mediaType,
                        options = MediaType.entries,
                        onValueChange = { mediaType = it },
                        placeholder = "Media Type",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Upload section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        uploadStatus = "Uploading..."
                        errorMessage = ""

                        GlobalScope.launch {
                            uploadService.uploadBook(
                                itemId = id,
                                title = title,
                                description = description,
                                longDescription = longDescription,
                                creators = creators,
                                subjects = subjects,
                                publishers = publishers,
                                languages = languages,
                                collections = collections,
                                translators = translators,
                                coverFilePath = coverFilePath,
                                epubFilePath = epubFilePath,
                                mediaType = mediaType,
                                copyright = copyright
                            ).onSuccess {
                                uploadStatus = "Upload successful!"
                            }.onFailure {
                                errorMessage = "Upload failed: ${it.message}"
                            }
                        }
                    },
                    modifier = Modifier.widthIn(min = 120.dp),
                    enabled = id.isNotEmpty() && title.isNotEmpty() && epubFilePath.isNotEmpty()
                ) {
                    Text("Upload Book")
                }

                if (uploadStatus.isNotEmpty()) {
                    Text(
                        uploadStatus,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.hoverable(remember { MutableInteractionSource() })
                            .pointerHoverIcon(PointerIcon(Cursor(Cursor.TEXT_CURSOR)))
                    )
                }
                if (errorMessage.isNotEmpty()) {
                    SelectionContainer {
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.hoverable(remember { MutableInteractionSource() })
                                .pointerHoverIcon(PointerIcon(Cursor(Cursor.TEXT_CURSOR)))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LongDescription(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = "Long Description",
        modifier = modifier,
        minLines = 10
    )
}

@Composable
private fun ResizableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minHeight: Dp = 100.dp,
    maxHeight: Dp = 500.dp,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE
) {
    Box {
        var textFieldHeight by remember { mutableStateOf(minHeight) }
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            modifier = modifier
                .fillMaxWidth()
                .height(textFieldHeight)
                .padding(bottom = 12.dp),
            minLines = minLines,
            maxLines = maxLines
        )

        ResizeHandle(
            isHovered = isHovered,
            interactionSource = interactionSource,
            onResize = { delta ->
                textFieldHeight = (textFieldHeight + delta.dp)
                    .coerceIn(minHeight, maxHeight)
            }
        )
    }
}

@Composable
private fun BoxScope.ResizeHandle(
    isHovered: Boolean,
    interactionSource: MutableInteractionSource,
    onResize: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(16.dp)
            .hoverable(interactionSource)
            .pointerHoverIcon(PointerIcon(Cursor(Cursor.SE_RESIZE_CURSOR)))
            .pointerInput(Unit) {
                var lastPosition: Float? = null

                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Press -> {
                                lastPosition = event.changes.first().position.y
                                event.changes.first().consumeAllChanges()
                            }

                            PointerEventType.Move -> {
                                lastPosition?.let { last ->
                                    val current = event.changes.first().position.y
                                    val delta = current - last
                                    onResize(delta)
                                    lastPosition = current
                                    event.changes.first().consumeAllChanges()
                                }
                            }

                            PointerEventType.Release -> {
                                lastPosition = null
                            }

                            else -> Unit
                        }
                    }
                }
            }
    ) {
        if (isHovered) {
            ResizeIndicator()
        }
    }
}

@Composable
private fun BoxScope.ResizeIndicator() {
    Box(
        modifier = Modifier
            .size(8.dp)
            .align(Alignment.Center)
            .background(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                shape = CircleShape
            )
    )
}

private fun chooseFile(
    fileType: String = "all",
    onFileSelected: (String) -> Unit
) {
    val frame = java.awt.Frame()
    val fileDialog = java.awt.FileDialog(frame).apply {
        when (fileType.lowercase()) {
            "epub" -> {
                setFilenameFilter { _, name -> name.endsWith(".epub", ignoreCase = true) }
                title = "Select EPUB File"
            }

            "image" -> {
                setFilenameFilter { _, name ->
                    name.endsWith(".jpg", ignoreCase = true) ||
                            name.endsWith(".jpeg", ignoreCase = true) ||
                            name.endsWith(".png", ignoreCase = true)
                }
                title = "Select Cover Image"
            }

            "opf" -> {
                setFilenameFilter { _, name -> name.endsWith(".opf", ignoreCase = true) }
                title = "Select content.opf File"
            }

            else -> {
                title = "Select File"
            }
        }
        isVisible = true
    }

    fileDialog.file?.let { fileName ->
        val filePath = "${fileDialog.directory}$fileName"
        onFileSelected(filePath)
    }

    fileDialog.dispose()
    frame.dispose()
}
