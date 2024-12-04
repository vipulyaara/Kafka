package com.kafka.kms.ui.upload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.kms.components.LongTextField
import com.kafka.kms.components.TextField
import com.kafka.kms.ui.upload.components.FileAndMediaSection
import com.kafka.kms.ui.upload.components.MainBookInfo
import com.kafka.kms.ui.upload.components.MetadataFields
import com.kafka.kms.ui.upload.components.UploadBottomBar

@Composable
fun UploadScreen(uploadViewModel: UploadViewModel, modifier: Modifier = Modifier) {
    val state = remember { UploadScreenState() }

    LaunchedEffect(state.title, state.creators) {
        state.updateIdFromTitleAndCreator()
    }

    LaunchedEffect(state.contentOpfPath) {
        if (state.contentOpfPath.isNotEmpty()) {
            uploadViewModel.parseContentOpf(state.contentOpfPath)
        }
    }

    LaunchedEffect(uploadViewModel.contentOpfState.value) {
        when (val opfState = uploadViewModel.contentOpfState.value) {
            is ContentOpfState.Success -> state.updateFromContentOpf(opfState.metadata)
            else -> Unit
        }
    }

    LaunchedEffect(uploadViewModel.fetchState.value) {
        when (val fetchState = uploadViewModel.fetchState.value) {
            is FetchState.Success -> state.updateFromFetchedItem(fetchState.data)
            else -> Unit
        }
    }

    Surface {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 24.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                UploadScreenHeader(
                    state = state,
                    onFetch = { uploadViewModel.fetchItem(state.id) },
                    isFetching = uploadViewModel.fetchState.value is FetchState.Loading
                )

                MainBookInfo(
                    title = state.title,
                    onTitleChange = { state.title = it },
                    creators = state.creators,
                    onCreatorsChange = { state.creators = it },
                    description = state.description,
                    onDescriptionChange = { state.description = it },
                    id = state.id,
                    onIdChange = { state.id = it },
                    onIdEdited = { state.hasEditedId = true },
                    publishers = state.publishers,
                    onPublishersChange = { state.publishers = it },
                    translators = state.translators,
                    onTranslatorsChange = { state.translators = it },
                    modifier = Modifier.fillMaxWidth()
                )

                LongDescription(
                    value = state.longDescription,
                    onValueChange = { state.longDescription = it },
                    modifier = Modifier.fillMaxWidth()
                )

                MetadataFields(
                    subjects = state.subjects,
                    onSubjectsChange = { state.subjects = it },
                    collections = state.collections,
                    onCollectionsChange = { state.collections = it },
                    languages = state.languages,
                    onLanguagesChange = { state.languages = it },
                    sourceUrls = state.sourceUrls,
                    onSourceUrlsChange = { state.sourceUrls = it },
                    copyright = state.copyrightText,
                    onCopyrightChange = { state.copyrightText = it },
                    dateFeatured = state.dateFeatured,
                    onDateFeaturedChange = {
                        if (it.isEmpty() || it.matches(Regex("""^\d{4}-\d{2}-\d{2}$"""))) {
                            state.dateFeatured = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                FileAndMediaSection(
                    contentOpfPath = state.contentOpfPath,
                    onContentOpfSelected = { state.contentOpfPath = it },
                    epubFilePath = state.epubFilePath,
                    onEpubSelected = { state.epubFilePath = it },
                    mediaType = state.mediaType,
                    onMediaTypeSelected = { state.mediaType = it },
                    coverImagePaths = state.coverImagePaths,
                    onCoverImageAdded = { state.coverImagePaths += it },
                    onCoverImageRemoved = { 
                        state.coverImagePaths = state.coverImagePaths.filter { path -> path != it }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            UploadBottomBar(
                isEnabled = when {
                    state.isEditMode -> state.id.isNotEmpty() && state.title.isNotEmpty()
                    else -> state.id.isNotEmpty() && state.title.isNotEmpty() && state.epubFilePath.isNotEmpty()
                } && uploadViewModel.uploadState.value !is UploadState.Loading,
                onUploadClick = {
                    uploadViewModel.uploadBook(
                        itemId = state.id,
                        title = state.title,
                        description = state.description,
                        longDescription = state.longDescription,
                        creators = state.creators,
                        subjects = state.subjects,
                        publishers = state.publishers,
                        languages = state.languages,
                        collections = state.collections,
                        translators = state.translators,
                        coverImagePaths = state.coverImagePaths,
                        epubFilePath = state.epubFilePath,
                        mediaType = state.mediaType,
                        copyright = state.copyrightText,
                        isUpdate = state.isEditMode,
                        isCopyrighted = state.isCopyrighted
                    )
                },
                onClearClick = {
                    state.clearFields()
                    uploadViewModel.resetAllStates()
                },
                uploadStatus = when (uploadViewModel.uploadState.value) {
                    is UploadState.Loading -> "Uploading..."
                    is UploadState.Success -> if (state.isEditMode) "Update successful!" else "Upload successful!"
                    else -> ""
                },
                errorMessage = getErrorMessage(uploadViewModel, state.isEditMode),
                isEditMode = state.isEditMode,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun UploadScreenHeader(
    state: UploadScreenState,
    onFetch: () -> Unit,
    isFetching: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = if (state.isEditMode) "Edit Book" else "Upload New Book",
            style = MaterialTheme.typography.headlineMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = state.id,
                onValueChange = {
                    state.id = it
                    state.hasEditedId = true
                },
                placeholder = "Enter ID to fetch",
                modifier = Modifier.width(400.dp),
                maxLines = 1
            )

            Button(
                onClick = onFetch,
                enabled = state.id.isNotEmpty() && !isFetching,
                modifier = Modifier.height(40.dp)
            ) {
                if (isFetching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Fetch")
                }
            }
        }
    }
}

private fun getErrorMessage(
    uploadViewModel: UploadViewModel,
    isEditMode: Boolean
): String = when {
    uploadViewModel.uploadState.value is UploadState.Error -> {
        val error = (uploadViewModel.uploadState.value as UploadState.Error).message
        if (isEditMode) "Update failed: $error" else "Upload failed: $error"
    }
    uploadViewModel.fetchState.value is FetchState.Error -> {
        "Failed to fetch item: ${(uploadViewModel.fetchState.value as FetchState.Error).message}"
    }
    uploadViewModel.contentOpfState.value is ContentOpfState.Error -> {
        "Failed to parse content.opf: ${(uploadViewModel.contentOpfState.value as ContentOpfState.Error).message}"
    }
    else -> ""
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
