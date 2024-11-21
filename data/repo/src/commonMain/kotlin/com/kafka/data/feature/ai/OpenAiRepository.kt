package com.kafka.data.feature.ai

import com.kafka.base.ApplicationScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class OpenAiRepository(private val openAi: OpenAi) {

    fun observerSummary(
        title: String,
        author: String? = null,
        language: String? = null,
    ): Flow<SummaryResponse> {
        var result = ""

        val request = OpenAi.Request(
            systemMessage = systemMessage,
            userMessage = summaryPrompt(title = title, author = author, language = language)
        )

        return openAi.chatCompletions(request)
            .onEach { result += it.choices.firstOrNull()?.delta?.content.orEmpty() }
            .map { SummaryResponse(result, it.choices.firstOrNull()?.finishReason != null) }
            .catch { it.printStackTrace() }
    }

    private val systemMessage = """
        You are a highly skilled literary analyst. Your task is to create detailed and comprehensive summaries of books. 
        Ensure that the summaries capture all essential plot points, character arcs, and themes while remaining concise. 
        The goal is to provide a summary that gives readers a full understanding of the book’s content without needing to read the entire text.
        The text length should be more than 2000 words.
    """.trimIndent()

    private fun summaryPrompt(title: String, author: String? = null, language: String? = null) = """
        Please write a 2,000-word summary of the book **$title** by author $author (author can be empty or wrong) in $language language.
            1. Start with an introduction that sets the stage for the story.
            2. Provide a detailed summary of each key chapter, focusing on the main events.
            3. Describe the main characters, their motivations, and their arcs throughout the story.
            4. Discuss the major themes and how they are explored in the book.
            5. Conclude with the resolution of the story and its overall impact.

        Each section should contribute to a detailed, comprehensive summary that takes about 10 minutes to read.
        Use beautiful markdown but do not show title and author on top in big heading.
    """.trimIndent()

}

data class SummaryResponse(val content: String, val finished: Boolean)
