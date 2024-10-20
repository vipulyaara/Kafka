package com.kafka.reader.epub

import androidx.compose.runtime.Composable

@Composable
expect fun EpubReader(fileId: String, viewModel: EpubReaderViewModel)