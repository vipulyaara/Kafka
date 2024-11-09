package kafka.reader.core.parser

import com.fleeksoft.ksoup.nodes.Node

class DocumentTraverser(
    private val startNode: Node,
    private val endNode: Node?
) {
    fun traverse(): List<Node> {
        val elements = mutableListOf<Node>()
        var current: Node? = startNode
        
        while (current != null && current != endNode) {
            elements.add(current)
            current = getNextNode(current)
        }
        
        return elements
    }

    private fun getNextNode(currentNode: Node?): Node? {
        var nextSibling: Node? = currentNode?.nextSibling()

        if (nextSibling == null) {
            var parentNode = currentNode?.parent()
            while (parentNode != null) {
                nextSibling = parentNode.nextSibling()
                if (nextSibling != null) {
                    return traverseDescendants(nextSibling)
                }
                parentNode = parentNode.parent()
            }
        }

        return nextSibling
    }

    private fun traverseDescendants(node: Node): Node? {
        val children = node.childNodes()
        if (children.isNotEmpty()) {
            return children.first()
        }

        val siblings = node.nextSiblingNodes()
        if (siblings.isNotEmpty()) {
            return traverseDescendants(siblings.first())
        }

        return null
    }
}
