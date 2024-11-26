package com.kafka.kms.ui.upload.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kafka.common.image.Icons
import com.kafka.data.model.MediaType
import java.io.File

@Composable
fun FileAndMediaSection(
    contentOpfPath: String,
    onContentOpfSelected: (String) -> Unit,
    epubFilePath: String,
    onEpubSelected: (String) -> Unit,
    mediaType: MediaType,
    onMediaTypeSelected: (MediaType) -> Unit,
    coverImagePaths: List<String>,
    onCoverImageAdded: (String) -> Unit,
    onCoverImageRemoved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FileSelectionButton(
                    label = "Content OPF File",
                    value = contentOpfPath,
                    onClick = { chooseFile("opf") { onContentOpfSelected(it) } },
                    icon = {
                        Icon(
                            imageVector = Icons.Plus,
                            contentDescription = "Select OPF file",
                            tint = if (contentOpfPath.isEmpty()) 
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else 
                                MaterialTheme.colorScheme.primary
                        )
                    }
                )
                
                FileSelectionButton(
                    label = "EPUB File",
                    value = epubFilePath,
                    onClick = { chooseFile("epub") { onEpubSelected(it) } },
                    icon = {
                        Icon(
                            Icons.Plus,
                            contentDescription = "Select EPUB file",
                            tint = if (epubFilePath.isEmpty()) 
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else 
                                MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MediaTypeSelector(
                    selected = mediaType,
                    onSelect = onMediaTypeSelected,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ImageSelectionGrid(
            images = coverImagePaths,
            onAddImage = onCoverImageAdded,
            onRemoveImage = onCoverImageRemoved,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun FileSelectionButton(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {
        Icon(
            Icons.Plus,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
) {
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .border(
                1.5.dp,
                if (value.isEmpty()) MaterialTheme.colorScheme.outline
                else MaterialTheme.colorScheme.primary,
                MaterialTheme.shapes.small
            )
            .clickable(onClick = onClick),
        color = if (value.isEmpty())
            MaterialTheme.colorScheme.surface
        else
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (value.isEmpty())
                            MaterialTheme.colorScheme.surfaceVariant
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.titleSmall
                )
                if (value.isNotEmpty()) {
                    Text(
                        value.substringAfterLast('/'),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        "No file selected",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageSelectionGrid(
    images: List<String>,
    onAddImage: (String) -> Unit,
    onRemoveImage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.height(200.dp)
    ) {
        items(images) { imagePath ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small)
                    .border(
                        1.5.dp,
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.small
                    )
            ) {
                val context = LocalPlatformContext.current
                val imageFile = remember(imagePath) { File(imagePath) }

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageFile)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Cover image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Add a semi-transparent overlay to make the close button more visible
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
                )

                IconButton(
                    onClick = { onRemoveImage(imagePath) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.XCircle,
                        contentDescription = "Remove image",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small)
                    .border(
                        1.5.dp,
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.small
                    )
                    .clickable {
                        chooseFile("image") { path ->
                            onAddImage(path)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Plus,
                    contentDescription = "Add image",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun MediaTypeSelector(
    selected: MediaType,
    onSelect: (MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MediaTypeOption(
            type = MediaType.Text,
            selected = selected == MediaType.Text,
            onClick = { onSelect(MediaType.Text) },
            icon = {
                Icon(
                    imageVector = Icons.Book,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier.weight(1f)
        )

        MediaTypeOption(
            type = MediaType.Audio,
            selected = selected == MediaType.Audio,
            onClick = { onSelect(MediaType.Audio) },
            icon = {
                Icon(
                    imageVector = Icons.Audio,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
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


@Composable
private fun MediaTypeOption(
    type: MediaType,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .defaultMinSize(minHeight = 80.dp)
            .clip(MaterialTheme.shapes.small)
            .border(
                width = if (selected) 2.dp else 1.5.dp,
                color = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
            .clickable(onClick = onClick),
        color = if (selected)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        else
            MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    icon()
                }
            }

            Text(
                type.name,
                style = MaterialTheme.typography.titleSmall,
                color = if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
