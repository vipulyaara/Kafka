package com.kafka.kms.ui.upload

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.kafka.kms.components.LongTextField
import com.kafka.kms.components.TextField
import com.kafka.kms.data.remote.SupabaseUploadService
import com.kafka.kms.ui.upload.components.FileAndMediaSection
import com.kafka.kms.ui.upload.components.MainBookInfo
import com.kafka.kms.ui.upload.components.MetadataFields
import com.kafka.kms.ui.upload.components.UploadBottomBar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Cursor

@Composable
fun UploadScreen(uploadService: SupabaseUploadService, modifier: Modifier = Modifier) {
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
    var coverImagePaths by remember { mutableStateOf(listOf<String>()) }
    var copyright by remember { mutableStateOf("") }
    var mediaType by remember { mutableStateOf(MediaType.Text) }
    var uploadStatus by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var hasEditedId by remember { mutableStateOf(false) }
    var contentOpfPath by remember { mutableStateOf("") }
    var dateFeatured by remember { mutableStateOf("") }

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
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp)
                    .padding(bottom = 80.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text("Upload New Book", style = MaterialTheme.typography.headlineMedium)

                MainBookInfo(
                    title = title,
                    onTitleChange = { title = it },
                    creators = creators,
                    onCreatorsChange = { creators = it },
                    description = description,
                    onDescriptionChange = { description = it },
                    id = id,
                    onIdChange = { id = it },
                    onIdEdited = { hasEditedId = true },
                    publishers = publishers,
                    onPublishersChange = { publishers = it },
                    translators = translators,
                    onTranslatorsChange = { translators = it },
                    modifier = Modifier.fillMaxWidth()
                )

                LongDescription(
                    value = longDescription,
                    onValueChange = { longDescription = it },
                    modifier = Modifier.fillMaxWidth()
                )

                MetadataFields(
                    subjects = subjects,
                    onSubjectsChange = { subjects = it },
                    collections = collections,
                    onCollectionsChange = { collections = it },
                    languages = languages,
                    onLanguagesChange = { languages = it },
                    sourceUrls = sourceUrls,
                    onSourceUrlsChange = { sourceUrls = it },
                    copyright = copyright,
                    onCopyrightChange = { copyright = it },
                    dateFeatured = dateFeatured,
                    onDateFeaturedChange = {
                        if (it.isEmpty() || it.matches(Regex("""^\d{4}-\d{2}-\d{2}$"""))) {
                            dateFeatured = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                FileAndMediaSection(
                    contentOpfPath = contentOpfPath,
                    onContentOpfSelected = { contentOpfPath = it },
                    epubFilePath = epubFilePath,
                    onEpubSelected = { epubFilePath = it },
                    mediaType = mediaType,
                    onMediaTypeSelected = { mediaType = it },
                    coverImagePaths = coverImagePaths,
                    onCoverImageAdded = { coverImagePaths = coverImagePaths + it },
                    onCoverImageRemoved = {
                        coverImagePaths = coverImagePaths.filter { path -> path != it }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            UploadBottomBar(
                isEnabled = id.isNotEmpty() && title.isNotEmpty() && epubFilePath.isNotEmpty(),
                onUploadClick = {
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
                            coverImagePaths = coverImagePaths,
                            epubFilePath = epubFilePath,
                            mediaType = mediaType,
                            copyright = copyright
                        ).onSuccess {
                            uploadStatus = "Upload successful!"
                        }.onFailure { error ->
                            errorMessage = "Upload failed: ${error.message}"
                        }
                    }
                },
                uploadStatus = uploadStatus,
                errorMessage = errorMessage,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun LongDescription(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    LongTextField(
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
