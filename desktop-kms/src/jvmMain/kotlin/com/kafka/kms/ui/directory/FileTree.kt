package com.kafka.kms.ui.directory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.TablerIcons
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.ChevronRight
import compose.icons.tablericons.FileText
import compose.icons.tablericons.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ui.common.theme.theme.Dimens
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds

data class FileTreeNode(
    val file: File,
    val level: Int,
    var isExpanded: Boolean = false
)

@Composable
fun FileTree(
    rootPath: String,
    modifier: Modifier = Modifier,
    onFileSelected: (File) -> Unit
) {
    var nodes by remember { mutableStateOf(listOf<FileTreeNode>()) }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(rootPath) {
        val rootFile = File(rootPath)
        nodes = listOf(FileTreeNode(rootFile, 0))
        
        // Start watching directory changes
        scope.launch(Dispatchers.IO) {
            val watchService = FileSystems.getDefault().newWatchService()
            val path = Paths.get(rootPath)
            path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY
            )
            
            while (isActive) {
                val key = watchService.take()
                val needsRefresh = key.pollEvents().any { event ->
                    event.kind() != StandardWatchEventKinds.OVERFLOW
                }
                
                if (needsRefresh) {
                    // Refresh the entire tree while maintaining expanded states
                    val expandedPaths = nodes.filter { it.isExpanded }.map { it.file.absolutePath }
                    val rootNode = FileTreeNode(rootFile, 0)
                    nodes = regenerateTree(rootNode, expandedPaths)
                }
                
                key.reset()
            }
        }
    }

    Surface(modifier = modifier.fillMaxHeight(), color = MaterialTheme.colorScheme.surfaceVariant) {
        LazyColumn(modifier = Modifier.fillMaxHeight().padding(Dimens.Gutter)) {
            items(nodes) { node ->
                FileTreeItem(
                    node = node,
                    onNodeClick = { clickedNode ->
                        if (clickedNode.file.isDirectory) {
                            nodes = updateNodes(nodes, clickedNode)
                        } else {
                            onFileSelected(clickedNode.file)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun FileTreeItem(
    node: FileTreeNode,
    onNodeClick: (FileTreeNode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { onNodeClick(node) }
            .padding(start = (node.level * 16).dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (node.file.isDirectory) {
            Icon(
                imageVector = if (node.isExpanded) FeatherIcons.ChevronDown else FeatherIcons.ChevronRight,
                contentDescription = if (node.isExpanded) "Collapse folder" else "Expand folder",
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(Dimens.Spacing04))
            Icon(
                imageVector = TablerIcons.Folder,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Spacer(modifier = Modifier.width(20.dp))
            Icon(
                imageVector = TablerIcons.FileText,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = node.file.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun updateNodes(
    currentNodes: List<FileTreeNode>,
    clickedNode: FileTreeNode
): List<FileTreeNode> {
    val mutableNodes = currentNodes.toMutableList()
    val nodeIndex = mutableNodes.indexOf(clickedNode)

    if (nodeIndex == -1) return currentNodes

    val node = mutableNodes[nodeIndex]
    node.isExpanded = !node.isExpanded

    if (node.isExpanded) {
        val childFiles = node.file.listFiles()?.sortedWith(
            compareBy({ !it.isDirectory }, { it.name.lowercase() })
        ) ?: emptyList()

        val childNodes = childFiles.map {
            FileTreeNode(it, node.level + 1)
        }
        mutableNodes.addAll(nodeIndex + 1, childNodes)
    } else {
        // Remove all child nodes
        val endIndex = findEndIndex(mutableNodes, nodeIndex, node.level)
        mutableNodes.subList(nodeIndex + 1, endIndex).clear()
    }

    return mutableNodes
}

private fun findEndIndex(nodes: List<FileTreeNode>, startIndex: Int, baseLevel: Int): Int {
    var endIndex = startIndex + 1
    while (endIndex < nodes.size && nodes[endIndex].level > baseLevel) {
        endIndex++
    }
    return endIndex
}

private fun regenerateTree(
    rootNode: FileTreeNode,
    expandedPaths: List<String>
): List<FileTreeNode> {
    val result = mutableListOf<FileTreeNode>()
    fun addNode(node: FileTreeNode) {
        result.add(node)
        if (expandedPaths.contains(node.file.absolutePath) && node.file.isDirectory) {
            node.isExpanded = true
            val childFiles = node.file.listFiles()?.sortedWith(
                compareBy({ !it.isDirectory }, { it.name.lowercase() })
            ) ?: emptyList()
            
            childFiles.forEach { childFile ->
                addNode(FileTreeNode(childFile, node.level + 1))
            }
        }
    }
    
    addNode(rootNode)
    return result
}

