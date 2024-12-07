package kafka.reader.core.parser

import com.fleeksoft.ksoup.nodes.Element
import com.kafka.base.debug
import kafka.reader.core.models.enums.TextStyle

class StylesheetParser(private val cssContent: String) {
    private val styleRules: Map<String, Map<String, String>> = parseCssContent(removeComments(cssContent)).also {
        it.forEach { (selector, styles) ->
            debug { "Added CSS rule: $selector -> $styles" }
        }
    }

    private fun removeComments(css: String): String {
        // Remove multi-line comments /* ... */
        val noMultilineComments = css.replace(Regex("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/"), "")
        
        // Remove single-line comments // ...
        return noMultilineComments.lines()
            .map { line -> line.replace(Regex("//.*$"), "") }
            .filter { it.isNotBlank() }
            .joinToString("\n")
    }

    fun getApplicableStyles(element: Element): Map<String, String> {
        val styles = mutableMapOf<String, String>()
        
        // Add default tag-based styles
        when (element.tagName().lowercase()) {
            "em", "i" -> styles["font-style"] = "italic"
            "strong", "b" -> styles["font-weight"] = "bold"
            "u" -> styles["text-decoration"] = "underline"
            "s", "strike", "del" -> styles["text-decoration"] = "line-through"
            "h1", "h2", "h3", "h4", "h5", "h6" -> styles["font-weight"] = "bold"
            // Add more tag-based styles as needed
        }
        
        // Add styles from CSS rules
        styles.putAll(findMatchingRules(element))
        
        // Add inline styles (highest priority)
        element.attr("style").split(";")
            .filter { it.isNotEmpty() }
            .forEach { declaration ->
                val parts = declaration.split(":")
                if (parts.size == 2) {
                    styles[parts[0].trim()] = parts[1].trim()
                }
            }
            
        return styles
    }

    fun applyStyles(element: Element) {
        val styles = getApplicableStyles(element)
        val styleString = styles.entries.joinToString(";") { (property, value) ->
            "$property:$value"
        }
        if (styleString.isNotEmpty()) {
            element.attr("style", styleString)
        }
    }

    private fun parseCssContent(css: String): Map<String, Map<String, String>> {
        debug { "Parsing CSS content: $css" }
        val rules = mutableMapOf<String, MutableMap<String, String>>()
        
        // Basic CSS parsing - split into rules
        val ruleRegex = """([^{]+)\{([^}]+)\}""".toRegex()
        
        ruleRegex.findAll(css).apply {
            debug { "CSS rules found: ${count()}" }
        }.forEach { matchResult ->
            val selectors = matchResult.groupValues[1].split(",").map { it.trim() }
            val declarations = matchResult.groupValues[2]

            debug { "CSS rule selectors: $selectors" }
            // Parse declarations into property-value pairs
            val styles = mutableMapOf<String, String>()
            declarations.split(";").forEach { declaration ->
                debug { "declaration: $declaration" }
                val parts = declaration.split(":")
                if (parts.size == 2) {
                    styles[parts[0].trim()] = parts[1].trim()
                }
            }
            
            // Add the styles for each individual selector
            selectors.forEach { selector ->
                rules[selector] = styles.toMutableMap()
                debug { "Added CSS rule for selector: $selector -> $styles" }
            }
        }
        
        return rules
    }

    private fun findMatchingRules(element: Element): Map<String, String> {
        val matchingStyles = mutableMapOf<String, String>()
        
        // Check element against CSS selectors
        for ((selector, styles) in styleRules) {
            if (elementMatchesComplexSelector(element, selector)) {
                matchingStyles.putAll(styles)
            }
        }
        
        return matchingStyles
    }

    private fun elementMatchesComplexSelector(element: Element, selector: String): Boolean {
        // Split selector into parts (handling both descendant and child combinators)
        val selectorParts = selector.split(">").map { it.trim() }
        
        var currentElement: Element? = element
        var partIndex = selectorParts.lastIndex
        
        while (currentElement != null && partIndex >= 0) {
            val selectorPart = selectorParts[partIndex]
            
            // Check if current element matches the current selector part
            if (!elementMatchesSimpleSelector(currentElement, selectorPart)) {
                if (partIndex == selectorParts.lastIndex) {
                    return false // The target element must match
                }
                
                // For ancestor parts, try to find a matching ancestor
                var ancestor = currentElement.parent()
                var found = false
                while (ancestor != null) {
                    if (elementMatchesSimpleSelector(ancestor, selectorPart)) {
                        found = true
                        currentElement = ancestor
                        break
                    }
                    ancestor = ancestor.parent()
                }
                
                if (!found) return false
            }
            
            // Move to next part
            currentElement = if (partIndex > 0) {
                // For child combinator (>), use immediate parent
                // For descendant combinator (space), parent was already handled above
                currentElement?.parent()
            } else null
            
            partIndex--
        }
        
        return partIndex < 0
    }

    private fun elementMatchesSimpleSelector(element: Element, selector: String): Boolean {
        // Handle universal selector
        if (selector == "*") return true
        
        // Split compound selectors (e.g., "p.class1.class2")
        val parts = selector.split(".").filter { it.isNotEmpty() }
        if (parts.isEmpty()) return false
        
        // Check tag name if specified
        if (!parts[0].contains("#")) {
            val tagName = parts[0]
            // Skip tag name check if it's a wildcard
            if (tagName.isNotEmpty() && tagName != "*" && !element.tagName().equals(tagName, ignoreCase = true)) {
                return false
            }
        }
        
        // Check ID if specified
        val idSelector = parts.find { it.startsWith("#") }
        if (idSelector != null && element.id() != idSelector.substring(1)) {
            return false
        }
        
        // Check classes
        val classSelectors = parts.filter { !it.contains("#") && it != parts[0] }
        return classSelectors.all { element.hasClass(it) }
    }
} 
