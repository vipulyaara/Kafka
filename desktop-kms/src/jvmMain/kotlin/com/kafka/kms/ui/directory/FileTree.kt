package com.kafka.kms.ui.directory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import compose.icons.tablericons.ChevronDown
import compose.icons.tablericons.ChevronUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ui.common.theme.theme.Dimens
import java.awt.Desktop
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
    onFileSelected: (File) -> Unit,
    defaultExpandedDirs: List<String> = emptyList()
) {
    var nodes by remember { mutableStateOf(listOf<FileTreeNode>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(rootPath) {
        val rootFile = File(rootPath)
        nodes = generateInitialTree(rootFile, defaultExpandedDirs)

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
        Column(modifier = Modifier.fillMaxHeight()) {
            // Add expand/collapse controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Gutter, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable {
                            nodes = expandCollapseAll(nodes, true)
                        }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = TablerIcons.ChevronDown,
                        contentDescription = "Expand",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Expand",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(Modifier.width(16.dp))
                
                Row(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable {
                            nodes = expandCollapseAll(nodes, false)
                        }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = TablerIcons.ChevronUp,
                        contentDescription = "Collapse",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Collapse",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Existing LazyColumn
            LazyColumn(modifier = Modifier.fillMaxHeight().padding(Dimens.Gutter)) {
                items(nodes) { node ->
                    FileTreeItem(
                        node = node,
                        onNodeClick = { clickedNode ->
                            if (clickedNode.file.isDirectory) {
                                nodes = updateNodes(nodes, clickedNode)
                            } else {
                                onFileSelected(clickedNode.file)
                                openInSystemExplorer(clickedNode.file)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FileTreeItem(node: FileTreeNode, onNodeClick: (FileTreeNode) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { onNodeClick(node) }
            .padding(start = (node.level * 16).dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
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
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
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
        if ((expandedPaths.contains(node.file.absolutePath)) && node.file.isDirectory) {
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

private fun openInSystemExplorer(file: File) {
    try {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (file.exists()) {
                desktop.browseFileDirectory(file)
            }
        }
    } catch (e: Exception) {
        // Handle any errors that might occur when trying to open the file
        println("Error opening file in system explorer: ${e.message}")
    }
}

private fun generateInitialTree(
    rootFile: File,
    defaultExpandedDirs: List<String>
): List<FileTreeNode> {
    val result = mutableListOf<FileTreeNode>()
    
    fun addNode(file: File, level: Int) {
        val isExpanded = defaultExpandedDirs.any { dir -> 
            file.absolutePath.startsWith(dir) 
        }
        val node = FileTreeNode(file, level, isExpanded)
        result.add(node)
        
        if (isExpanded && file.isDirectory) {
            val childFiles = file.listFiles()?.sortedWith(
                compareBy({ !it.isDirectory }, { it.name.lowercase() })
            ) ?: emptyList()
            
            childFiles.forEach { childFile ->
                addNode(childFile, level + 1)
            }
        }
    }
    
    addNode(rootFile, 0)
    return result
}

private fun expandCollapseAll(
    nodes: List<FileTreeNode>,
    expand: Boolean
): List<FileTreeNode> {
    val rootNode = nodes.firstOrNull() ?: return nodes
    val result = mutableListOf<FileTreeNode>()
    
    fun processNode(file: File, level: Int) {
        val node = FileTreeNode(file, level, expand)
        result.add(node)
        
        if (expand && file.isDirectory) {
            val childFiles = file.listFiles()?.sortedWith(
                compareBy({ !it.isDirectory }, { it.name.lowercase() })
            ) ?: emptyList()
            
            childFiles.forEach { childFile ->
                processNode(childFile, level + 1)
            }
        }
    }
    
    processNode(rootNode.file, 0)
    return result
}
