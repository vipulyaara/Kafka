package com.kafka.kms.ui.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.kms.ui.upload.components.FileSelectionButton
import com.kafka.kms.ui.upload.components.chooseFile

@Composable
fun PrepareEpubScreen(prepareEpubViewModel: PrepareEpubViewModel, modifier: Modifier = Modifier) {
    var opfPath by remember { mutableStateOf("") }
    var xhtmlPath by remember { mutableStateOf("") }
    val prepareState = prepareEpubViewModel.prepareState.value

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
                Text(
                    text = "Prepare Epub Files",
                    style = MaterialTheme.typography.headlineMedium
                )

                FileSelectionButton(
                    label = "Content.opf File",
                    value = opfPath,
                    onClick = { 
                        chooseFile("opf") { opfPath = it }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                FileSelectionButton(
                    label = "XHTML File",
                    value = xhtmlPath,
                    onClick = {
                        chooseFile("xhtml") { xhtmlPath = it }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                when (prepareState) {
                    is PrepareState.Error -> Text(
                        text = prepareState.message,
                        color = MaterialTheme.colorScheme.error
                    )
                    is PrepareState.Success -> Text(
                        text = prepareState.message,
                        color = MaterialTheme.colorScheme.primary
                    )
                    else -> Unit
                }
            }

            // Bottom Bar
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        opfPath = ""
                        xhtmlPath = ""
                        prepareEpubViewModel.resetState()
                    },
                    enabled = opfPath.isNotEmpty() || xhtmlPath.isNotEmpty()
                ) {
                    Text("Clear")
                }

                Button(
                    onClick = { prepareEpubViewModel.prepareEpub(opfPath, xhtmlPath) },
                    enabled = xhtmlPath.isNotEmpty() &&
                             prepareState !is PrepareState.Loading
                ) {
                    if (prepareState is PrepareState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Prepare")
                    }
                }
            }
        }
    }
}

